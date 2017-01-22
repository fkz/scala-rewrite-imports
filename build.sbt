name := "rewrite-imports"
organization := "com.github.fkz"
version := "0.2.0"
scalaVersion := "2.11.8"
libraryDependencies += "org.scala-lang" % "scala-compiler" % scalaVersion.value

lazy val sbtProject = project.in(file("sbt")).settings(
  sbtPlugin := true,
  name := "sbt-rewrite-imports",
  organization := "com.github.fkz",
  version := "0.2.0",
  crossScalaVersions := List("2.10.6", "2.11.8")
)
