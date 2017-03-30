name := """json-schema-demo"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala, SbtWeb)

scalaVersion := "2.12.1"

JsEngineKeys.engineType := JsEngineKeys.EngineType.Node

libraryDependencies ++= Seq(
  jdbc,
  cache,
  ws,
  "com.eclipsesource" %% "play-json-schema-validator" % "0.9.0",
  "org.webjars" %% "webjars-play" % "2.6.0-M1",
  "org.webjars" % "npm" % "4.2.0",
  "org.webjars" % "angularjs" % "1.6.2"
)

resolvers += "emueller-bintray" at "http://dl.bintray.com/emueller/maven"

resolvers += "scalaz-bintray" at "http://dl.bintray.com/scalaz/releases"

// via https://stackoverflow.com/questions/28265069/cant-get-sbt-web-to-work-with-npm-for-frontend-dependencies
lazy val copyNodeModules = taskKey[Unit]("Copies the node_modules folder to the target directory")

copyNodeModules := {
  val node_modules = new File("node_modules")
  val target = new File("target/web/public/main/public/lib/")
  IO.copyDirectory(node_modules,target, overwrite = true, preserveLastModified = true)
}

