name := "producthunt-magiceightball"

version := "1.0"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.2"

libraryDependencies ++= Seq(
  jdbc,
  anorm,
  cache
)


libraryDependencies ++= Seq(
  "org.webjars" %% "webjars-play" % "2.3.0-2",
  "org.webjars" % "bootstrap" % "3.2.0-2",
  "mysql" % "mysql-connector-java" % "5.1.33"

)

TwirlKeys.templateImports += "views.html.helper._"

