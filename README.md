# Ray - Roy on Play 2.0

## About

Ray is a [Roy][] 'compiled asset plugin' for Play 2.0 - it is similar
to the built-in [CoffeeScript compiler][]. Roy files placed under
`app/assets/javascripts` will be requestable as if they were plain
JavaScript files.

For example, Roy code in `app/assets/javascripts/main.roy` will be
automatically compiled to JavaScript when requested via
`public/javascripts/main.js`:

![](http://i.imgur.com/G0qpG.png)

Compile time errors (such as static type errors) will cause a nice
Play error page:

![](http://i.imgur.com/wJwAp.png)

[Roy]: http://roy.brianmckenna.org/
[CoffeeScript compiler]: https://github.com/playframework/Play20/wiki/AssetsCoffeeScript

## Installation

First you must clone this Github repository locally.
The you must publish the plugin to your Play 2.0 repository.

You will have to specify your top level play directory and the version:

	sbt -Dplay.path=<play-top-level-directory> -Dplay.version=<play-version> publish-local

Or if you installed play with Homebrew:

	sbt -Dplay.path=/usr/local/Cellar/play -Dplay.version=<play-version> publish-local

Then add the plugin to your application's `project/plugins.sbt`:

    addSbtPlugin("org.brianmckenna" % "ray" % "0.2-play2.1-0426")

Now you can add an import to your application's `project/Build.scala`:

    import org.brianmckenna.ray.RoyBuild

And extend your `Build` with the `RoyBuild` trait:

    object ApplicationBuild extends Build with RoyBuild

Finally add the following lines to the `settings` call:

    royEntryPoints <<= (sourceDirectory in Compile)(base => base / "assets" ** "*.roy"),

    royOptions := Seq.empty[String],

    resourceGenerators in Compile <+= RoyCompiler
