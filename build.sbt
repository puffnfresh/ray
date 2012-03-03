sbtPlugin := true

name := "ray"

organization := "org.brianmckenna"

version := "0.1"

resolvers += playRepository

resolvers += typesafeRepository

addSbtPlugin("play" % "sbt-plugin" % playVersion)

publishTo := Some(playRepository)

publishMavenStyle := false
