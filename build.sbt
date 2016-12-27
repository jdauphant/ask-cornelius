name := "producthunt-magiceightball"

version := "1.0"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.8"

libraryDependencies ++= Seq(
  jdbc,
  cache
)


libraryDependencies ++= Seq(
  "org.webjars" %% "webjars-play" % "2.3.0-2",
  "com.typesafe.play" %% "anorm" % "2.4.0",
  "org.webjars" % "bootstrap" % "3.2.0-2",
  "mysql" % "mysql-connector-java" % "5.1.33"
)

TwirlKeys.templateImports += "views.html.helper._"

