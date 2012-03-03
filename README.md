# Ray - Roy on Play 2.0

## About

Ray is a Roy 'compiled asset plugin' for Play 2.0 - it is similar to
the built-in [CoffeeScript compiler][]. Roy files placed under
`app/assets/javascripts` will be requestable as if they were plain
JavaScript files.

For example, Roy code in `app/assets/javascripts/main.roy` will be
automatically compiled to JavaScript when requested via
`public/javascripts/main.js`:

![](http://i.imgur.com/G0qpG.png)

Compile time errors (such as static type errors) will cause a nice
Play error page:

![](http://i.imgur.com/wJwAp.png)

[CoffeeScript compiler]: https://github.com/playframework/Play20/wiki/AssetsCoffeeScript

## Installation

First you must publish the plugin to your Play 2.0 repository. You
will have to specify your top level play directory and the version:

    sbt -Dplay.path=../play-2.0-RC3 -Dplay.version=2.0-RC3 publish

Then add the plugin to your application's `project/plugins.sbt`:

    addSbtPlugin("org.brianmckenna" % "ray" % "0.1")

And finally add the following lines to the settings call in your
application's `project/Build.scala`:

    royEntryPoints <<= (sourceDirectory in Compile)(base => base / "assets" ** "*.roy"),

    royOptions := Seq.empty[String],

    resourceGenerators in Compile <+= RoyCompiler
