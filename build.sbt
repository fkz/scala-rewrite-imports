val currentVersion = "0.2.6"

lazy val scalacPlugin = (project in file("scalac-plugin")).settings(
  name := "rewrite-imports",
  organization := "io.github.fkz",
  version := currentVersion,
  libraryDependencies += "org.scala-lang" % "scala-compiler" % scalaVersion.value
).cross

lazy val scalacPlugin_2_12 = scalacPlugin("2.12.1").settings(name := "rewrite-imports")
lazy val scalacPlugin_2_11 = scalacPlugin("2.11.8").settings(name := "rewrite-imports")

lazy val sbtProject = project.in(file("sbtplugin"))
   .enablePlugins(BuildInfoPlugin)
   .settings(
  sbtPlugin := true,
  name := "sbt-rewrite-imports",
  organization := "io.github.fkz",
  version := currentVersion,
  scalaVersion := "2.10.6",
  buildInfoKeys := Seq(version),
  buildInfoPackage := "com.github.fkz"
)
