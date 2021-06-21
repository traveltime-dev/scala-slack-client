inThisBuild(List(
  organization := "com.traveltime",
  homepage := Some(url("https://github.com/traveltime-dev/scala-slack-client")),
  licenses := List("MIT License" -> url("https://github.com/traveltime-dev/scala-slack-client/blob/master/LICENSE.txt")),
  developers := List(
    Developer("donatas", "Donatas Laurinavičius", "donatas@traveltime.com", url("https://traveltime.com")),
    Developer("jonas", "Jonas Krutulis", "jonas.krutulis@traveltime.com", url("https://traveltime.com"))
  )
))

sonatypeCredentialHost := "s01.oss.sonatype.org"
sonatypeRepository := "https://s01.oss.sonatype.org/service/local"

name := "scala-slack-client"

crossScalaVersions := Seq("2.13.6", "2.12.14")

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
  "com.typesafe.play"  %% "play-json"     % "2.7.4",
  "org.dispatchhttp"   %% "dispatch-core" % "1.1.0",
  "org.apache.commons" %  "commons-lang3" % "3.9",
  "org.joda"           %  "joda-convert"  % "2.2.1",
  "org.scalatest"      %% "scalatest"     % "3.0.8"    % Test,
  "org.scalacheck"     %% "scalacheck"    % "1.14.0"   % Test
)

licenses += ("MIT", url("http://opensource.org/licenses/MIT"))
