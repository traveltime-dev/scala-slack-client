organization := "com.igeolise"

name := "scala-slack-client"

version := "2.0.0-SNAPSHOT"

scalacOptions ++= Seq(
  "-feature",
  "-deprecation",
  "-unchecked",
  "-Xfatal-warnings",
  "-Xlint",
  "-Ywarn-dead-code",
  "-Ywarn-numeric-widen",
  "-Ywarn-value-discard"
)

libraryDependencies ++= Seq(
  "com.typesafe.play"          %% "play-json"     % "2.7.4",
  "org.dispatchhttp"           %% "dispatch-core" % "1.1.0",
  "org.apache.commons"         %  "commons-lang3" % "3.9",
  "org.joda"                   %  "joda-convert"  % "2.2.1",
  "com.fasterxml.jackson.core" %  "jackson-core"  % "2.10.0",
  "org.scalatest"              %% "scalatest"     % "3.0.8"    % Test,
  "org.scalacheck"             %% "scalacheck"    % "1.14.0"   % Test
)

bintrayOrganization := Some("igeolise")

licenses += ("MIT", url("http://opensource.org/licenses/MIT"))
