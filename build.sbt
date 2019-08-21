organization := "com.igeolise"

name := "scala-slack-client"

scalacOptions ++= Seq(
  "-feature",
  "-deprecation",
  "-unchecked",
  "-Xfatal-warnings",
  "-Xlint",
  "-Yno-adapted-args",
  "-Ywarn-dead-code",
  "-Ywarn-numeric-widen",
  "-Ywarn-value-discard"
)

libraryDependencies ++= Seq(
  "com.typesafe.play"       %% "play-json"     % "2.7.4",
  "net.databinder.dispatch" %% "dispatch-core" % "0.13.4",
  "org.apache.commons"      %  "commons-lang3" % "3.9",
  "org.joda"                %  "joda-convert"  % "2.2.1",
  "org.scalatest"           %% "scalatest"     % "3.0.8"    % Test,
  "org.scalacheck"          %% "scalacheck"    % "1.14.0"   % Test
)

bintrayOrganization := Some("igeolise")

licenses += ("MIT", url("http://opensource.org/licenses/MIT"))
