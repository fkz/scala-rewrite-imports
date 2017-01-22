package com.github.fkz

import org.apache.ivy.util.filter.ArtifactTypeFilter
import sbt.{AllRequirements, AutoPlugin, File, PluginTrigger}
import sbt.Keys._
import sbt._

/**
  * Created by fabian on 11/23/16.
  */
object SbtImportRemovePlugin2 extends AutoPlugin {
  object autoImport {

  }

  override def trigger: PluginTrigger = AllRequirements

  override def projectSettings: Seq[_root_.sbt.Def.Setting[_]] = {
    Seq(
      scalacOptions += {
        val dep = update.value.toSeq.find {
          case (conf, moduleId, artifact, file) =>
            moduleId.organization == "com.github.fkz" && moduleId.name == "import-remove"
        }
        val v = dep.getOrElse(throw new Exception("fatal: sbt import plugin hasn't found scalac plugin import-remove"))
        s"-Xplugin:${v._4.getAbsolutePath}"
      },
      libraryDependencies += "com.github.fkz" %% "import-remove" % "1.0.0"
    )
  }
}
