name := """countries"""

version := "1.0"

scalaVersion := "2.11.5"

val sprayV = "1.3.1"

libraryDependencies ++= Seq(
  "io.spray" %% "spray-can" % sprayV
  , "io.spray" %% "spray-routing" % sprayV
  , "io.spray" %% "spray-testkit" % sprayV % "test"
  , "com.typesafe.akka" %% "akka-actor" % "2.3.9"
  , "com.typesafe.akka" %% "akka-testkit" % "2.3.9" % "test"
  , "org.specs2" %% "specs2-core" % "2.3.11" % "test"
  , "org.scalatest" %% "scalatest" % "2.2.4" % "test"
  , "ch.qos.logback"      % "logback-classic" % "1.0.13"
  , "io.spray" %% "spray-json" % "1.3.0"
  , "com.gettyimages" %% "spray-swagger" % "0.5.0"
)
