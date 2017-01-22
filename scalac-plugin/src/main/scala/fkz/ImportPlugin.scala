package fkz

import scala.tools.nsc.{Global, Phase}
import scala.tools.nsc.plugins.{Plugin, PluginComponent}
import scala.tools.nsc.transform.Transform

case class ImportPluginOptions(filterPrefix: List[String] = List.empty, mapPrefix: List[(String, String)] = List.empty)

class ImportPlugin(val global: Global) extends Plugin {
  override val name: String = "import"
  val removeImportComponent = new RemoveImportComponent(global)
  override val components: List[PluginComponent] = List(removeImportComponent)
  override val description: String = "remove imports that are not necessary"

  var passedOptions: ImportPluginOptions = _

  override def init(options: List[String], error: (String) => Unit): Boolean = {
    val filterR = "^filter:(.*)$".r
    val mapR = "^map:([^:]*):([^:]*)".r
    val opts = options.foldLeft(ImportPluginOptions()) { (opts, opt) =>
      opt match {
        case filterR(filterPrefix) => opts.copy(filterPrefix = filterPrefix :: opts.filterPrefix)
        case mapR(from, to) => opts.copy(mapPrefix = (from, to) :: opts.mapPrefix)
        case _ =>
          error(s"unknown option ${opt}, (need filter or map option)")
          opts
      }
    }

    removeImportComponent.options = opts
    true
  }

  override val optionsHelp: Option[String] = Some(
    List("-P:import:filter:<prefix> import statements that start with <prefix> get filtered",
         "-P:import:map:<prefixFrom>:<prefixTo> import statements that start with <prefixFrom> get mapped to <prefixTo>")
      .mkString("\n")
  )
}

class RemoveImportComponent(val global: Global) extends PluginComponent with Transform {
  var options: ImportPluginOptions = _

  override val phaseName: String = "import"
  override val runsAfter: List[String] = List("parser")
  override val runsBefore: List[String] = List("namer")

  def newTransformer(unit: global.CompilationUnit): global.Transformer = new Transformer(unit)

  def shouldFilter(importSelection: global.Tree): Option[global.Tree] = {
    import global._

    def toParts(x: global.Tree): List[String] = {
      x match {
        case Ident(TermName(s)) => List(s)
        case Select(qual, TermName(s)) => s :: toParts(qual)
      }
    }

    val str = toParts(importSelection).reverse.mkString(".") + "."

    if (options.filterPrefix.exists(str.startsWith)) {
      inform(importSelection.pos, s"filtering '${str}'")
      None
    }
    else {
      val subst = options.mapPrefix.find { case (from, to) => str.startsWith(from) }
      val substituted = {
        subst match {
          case None =>
            inform(importSelection.pos, s"no rule matches '${str}', leaving as is")
            str
          case Some((from, to)) =>
            val res = to + str.substring(from.length)
            inform(importSelection.pos, s"replacing '${str}' with '${res}")
            res
        }
      }

      val substitutedList = substituted.split('.')
      Some(substitutedList.tail.foldLeft[Tree](Ident(TermName(substitutedList.head)))((b, str) => Select(b, TermName(str))))
    }
  }

  class Transformer(unit: global.CompilationUnit) extends global.Transformer {
    override def transform(tree: global.Tree): global.Tree = {
      tree match {
        case global.Import(expr, selectors) =>
          shouldFilter(expr).map(global.Import(_, selectors)).getOrElse(global.EmptyTree)
        case _ => super.transform(tree)

      }
    }
  }
}
