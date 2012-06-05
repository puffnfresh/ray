sbtPlugin := true

name := "ray"

organization := "org.brianmckenna"

version := "0.2-play2.1-0426"

resolvers += playRepository

resolvers += typesafeRepository

addSbtPlugin("play" % "sbt-plugin" % playVersion)

publishTo := Some(playRepository)

publishMavenStyle := false
