import sbt._
import Keys._

val scioVersion = "0.7.4"
val beamVersion = "2.11.0"
val scalaMacrosVersion = "2.1.1"

lazy val commonSettings = Defaults.coreDefaultSettings ++ Seq(
  organization := "haken",
  version := "0.1.0-SNAPSHOT",
  scalaVersion := "2.11.11"
)

addCompilerPlugin("org.scalamacros" % "paradise" % "2.1.1" cross CrossVersion.full)

val circeVersion = "0.10.0"

lazy val root: Project = project
  .in(file("."))
  .settings(commonSettings)
//  .settings(macroSettings)
  .settings(
    name := "snowplow-raw-ips-job",
    description := "snowplow-raw-ips-job",
    publish / skip := true,
    libraryDependencies ++= Seq(
      "com.spotify" %% "scio-core" % scioVersion,
      "com.spotify" %% "scio-test" % scioVersion % Test,
      "org.apache.beam" % "beam-runners-direct-java" % beamVersion,
      // optional dataflow runner
      // "org.apache.beam" % "beam-runners-google-cloud-dataflow-java" % beamVersion,
      "org.slf4j" % "slf4j-simple" % "1.7.25"
    ),
//    libraryDependencies += "com.typesafe.play" %% "play-json" % "2.7.3",
    libraryDependencies += "com.snowplowanalytics" %% "snowplow-scala-analytics-sdk" % "0.4.1",
    libraryDependencies += "com.snowplowanalytics" %% "snowplow-common-enrich" % "0.37.0",
//    libraryDependencies += "com.snowplowanalytics" %% "iglu-scala-client" % "0.6.0",
      //    libraryDependencies += "io.circe" %% "circe-parser" % "0.11.1",
    libraryDependencies ++= Seq(
      "io.circe" %% "circe-core",
      "io.circe" %% "circe-generic",
      "io.circe" %% "circe-parser"
    ).map(_ % circeVersion)
  )
  .enablePlugins(PackPlugin)

lazy val repl: Project = project
  .in(file(".repl"))
  .settings(commonSettings)
//  .settings(macroSettings)
  .settings(
    name := "repl",
    description := "Scio REPL for snowplow-raw-ips-job",
    libraryDependencies ++= Seq(
      "com.spotify" %% "scio-repl" % scioVersion
    ),
    Compile / mainClass := Some("com.spotify.scio.repl.ScioShell"),
    publish / skip := true
  )
  .dependsOn(root)
