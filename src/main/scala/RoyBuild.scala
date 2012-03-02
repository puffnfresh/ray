package org.brianmckenna.ray

import sbt._
import PlayProject._

import play.api._

trait RoyBuild {

  val royEntryPoints = SettingKey[PathFinder]("play-roy-entry-points")
  val royOptions = SettingKey[Seq[String]]("play-roy-options")

  val RoyCompiler = AssetsCompiler("roy",
    royEntryPoints,
    { (name, min) => name.replace(".roy", if (min) ".min.js" else ".js") },
    { (royFile, options) =>
      import scala.util.control.Exception._
      val jsSource = org.brianmckenna.ray.RoyCompiler.compile(royFile, options)
      // Any error here would be because of Roy, not the developer;
      // so we don't want compilation to fail.
      val minified = catching(classOf[CompilationException])
        .opt(play.core.jscompile.JavascriptCompiler.minify(jsSource, Some(royFile.getName())))
      (jsSource, minified, Seq(royFile))
    },
    royOptions
  )

}

case class CompilationException(message: String, royFile: File, atLine: Option[Int]) extends PlayException(
  "Compilation error", message) with PlayException.ExceptionSource {
  def line = atLine
  def position = None
  def input = Some(scalax.file.Path(royFile))
  def sourceName = Some(royFile.getAbsolutePath)
}

object RoyCompiler {

  import java.io._

  import org.mozilla.javascript._
  import org.mozilla.javascript.tools.shell._

  import scala.collection.JavaConverters._

  import scalax.file._

  private lazy val compiler = {
    val ctx = Context.enter; ctx.setOptimizationLevel(-1)
    val global = new Global; global.init(ctx)
    val scope = ctx.initStandardObjects(global)

    val wrappedRoyCompiler = Context.javaToJS(this, scope)
    ScriptableObject.putProperty(scope, "RoyCompiler", wrappedRoyCompiler)

    val f = new File("/tmp/roy.js")
    val is = new FileInputStream(f)

    ctx.evaluateReader(scope,
      new InputStreamReader(is),
      "roy.js",
      1, null)

    val roy = scope.get("roy", scope).asInstanceOf[NativeObject]
    val compilerFunction = roy.get("compile", scope).asInstanceOf[Function]

    Context.exit

    (source: File) => {
      val royCode = Path(source).slurpString.replace("\r", "")
      val compiledObject = Context.call(null, compilerFunction, scope, scope, Array(royCode)).asInstanceOf[NativeObject]
      compiledObject.get("output", scope).asInstanceOf[String]
    }
    
  }

  def compile(source: File, options: Seq[String]): String = {
    try {
      compiler(source)
    } catch {
      case e: JavaScriptException => {

        val line = """.*on line ([0-9]+).*""".r
        val error = e.getValue.asInstanceOf[Scriptable]

        throw ScriptableObject.getProperty(error, "message").asInstanceOf[String] match {
          case msg @ line(l) => CompilationException(
            msg,
            source,
            Some(Integer.parseInt(l)))
          case msg => CompilationException(
            msg,
            source,
            None)
        }

      }
    }
  }

}
