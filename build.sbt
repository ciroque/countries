import com.typesafe.sbt.SbtNativePackager._

name := """countries"""

version := "1.0"

scalaVersion := "2.11.1"

val sprayVersion = "1.3.3"
val akkaVersion = "2.4.0"

libraryDependencies ++= Seq(
  "io.spray"                %% "spray-can"          % sprayVersion
  , "io.spray"              %% "spray-routing"      % sprayVersion
  , "io.spray"              %% "spray-testkit"      % sprayVersion      % "test"
  , "com.typesafe.akka"     %% "akka-actor"         % akkaVersion
  , "com.typesafe.akka"     %% "akka-testkit"       % akkaVersion % "test"
  , "org.specs2"            %% "specs2-core"        % "2.3.11"    % "test"
  , "org.scalatest"         %% "scalatest"          % "2.2.4"     % "test"
  , "ch.qos.logback"         % "logback-classic"    % "1.0.13"
  , "io.spray"              %% "spray-json"         % "1.3.0"
  , "com.gettyimages"       %% "spray-swagger"      % "0.5.1"
)

enablePlugins(JavaServerAppPackaging)

packageSummary := "Countries of the World microservice"

packageDescription := "Countries Service"

maintainer := "Steve Wagner <scalawagz@outlook.com>"

name := "countries"
