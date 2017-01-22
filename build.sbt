val currentVersion = "0.2.1"

lazy val scalacPlugin = (project in file("scalacPlugin")).settings(
  name := "rewrite-imports",
  organization := "io.github.fkz",
  version := currentVersion,
  scalaVersion := "2.11.8",
  libraryDependencies += "org.scala-lang" % "scala-compiler" % scalaVersion.value,
).cross

lazy val scalacPlugin_2_11 = scalacPlugin("2.11.8")
lazy val scalacPlugin_2_12 = scalacPlugin("2.12.1")

lazy val sbtProject = project.in(file("sbtplugin")).settings(
  sbtPlugin := true,
  name := "sbt-rewrite-imports",
  organization := "io.github.fkz",
  version := currentVersion,
  scalaVersion := "2.10.6",
)
