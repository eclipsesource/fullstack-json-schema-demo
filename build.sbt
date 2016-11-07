name := """fullstack-json-schema-demo"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala, SbtWeb)

scalaVersion := "2.11.7"

JsEngineKeys.engineType := JsEngineKeys.EngineType.Node

libraryDependencies ++= Seq(
  jdbc,
  cache,
  ws,
  "com.eclipsesource" %% "play-json-schema-validator" % "0.8.5",
  "org.scalatestplus.play" %% "scalatestplus-play" % "1.5.1" % Test,
  "org.webjars" %% "webjars-play" % "2.5.0",
  "org.webjars" % "npm" % "3.9.3",
  "org.webjars" % "angularjs" % "1.5.7"
)

resolvers += "emueller-bintray" at "http://dl.bintray.com/emueller/maven"

resolvers += "scalaz-bintray" at "http://dl.bintray.com/scalaz/releases"

// via https://stackoverflow.com/questions/28265069/cant-get-sbt-web-to-work-with-npm-for-frontend-dependencies
lazy val copyNodeModules = taskKey[Unit]("Copys the node_modules folder to the target directory")

copyNodeModules := {
  val node_modules = new File("node_modules")
  val target = new File("target/web/public/main/public/lib/")
  IO.copyDirectory(node_modules,target, overwrite = true, preserveLastModified = true)
}

addCommandAlias("getNpmDeps", ";web-assets:jseNpmNodeModules;copyNodeModules")
