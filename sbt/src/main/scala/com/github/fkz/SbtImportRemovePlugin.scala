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
    val importMappings = SettingKey[Seq[(String, Option[String])]]("import-mappings", "defines mappings of prefixes of imports. When None is used as second argument, imports get filtered")
  }

  override def trigger: PluginTrigger = AllRequirements

  override def projectSettings: Seq[_root_.sbt.Def.Setting[_]] = {
    Seq(
      autoImport.importMappings := Seq.empty,
      scalacOptions ++= {
        val dep = update.value.toSeq.find {
          case (conf, moduleId, artifact, file) =>
            moduleId.organization == "com.github.fkz" && moduleId.name.startsWith("rewrite-imports")
        }
        val v = dep.getOrElse(throw new Exception("fatal: sbt import plugin hasn't found scalac plugin rewrite-imports"))
        
        val im = autoImport.importMappings.value
        val mapImports = im.collect { 
          case (from, None) => s"-P:import:filter:$from" 
          case (from, Some(to)) => s"-P:import:map:$from:$to"
        }

        s"-Xplugin:${v._4.getAbsolutePath}" +: mapImports
      },
      libraryDependencies += "com.github.fkz" %% "rewrite-imports" % "0.2.0"
    )
  }
}
