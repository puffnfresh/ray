sbtPlugin := true

name := "ray"

organization := "org.brianmckenna"

version := "0.1"

resolvers += {
          val playPath = Option(System.getenv("PLAY_PATH")).getOrElse("../play")
          Resolver.file("Local Play Repository", file(new File(playPath, "repository/local").getPath))(Resolver.ivyStylePatterns)
}

resolvers += Resolver.url("Typesafe repository", url("http://repo.typesafe.com/typesafe/releases/"))(Resolver.ivyStylePatterns)

addSbtPlugin("play" % "sbt-plugin" % Option(System.getenv("PLAY_VERSION")).getOrElse("2.0"))

publishTo := {
          val playPath = Option(System.getenv("PLAY_PATH")).getOrElse("../play")
          Some(Resolver.file("Local Play Repository", file(new File(playPath, "repository/local").getPath))(Resolver.ivyStylePatterns))
}

publishMavenStyle := false
