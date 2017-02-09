Overview
========
[![Build Status](https://travis-ci.org/fkz/scala-rewrite-imports.svg?branch=master)](https://travis-ci.org/fkz/scala-rewrite-imports)
[![Latest version](https://index.scala-lang.org/fkz/scala-rewrite-imports/rewrite-imports/latest.svg)](https://index.scala-lang.org/fkz/scala-rewrite-imports/rewrite-imports)

This is a scalac plugin and corresponding sbt plugin, that lets you rewrite import statements depending on configuration.
One use case may be to use different libraries depending on configuration which in turn have different prefixes.
Unfortunately, macros can't be used for that, because imports can't be prefixed by an annotation.

Usage
=====
In the subfolder example, you see an example of how this plugin can be used.

Add the following to your `project/build.sbt`:

```scala
addSbtPlugin("io.github.fkz" % "sbt-rewrite-imports" % "0.3.1")
```

Then you can define mappings of import prefixes in your `build.sbt`:

```scala
importMappings += ("com.filter.this.prefix", None)
importMappings += ("com.somelibrary1", Some("com.replacementlibrary"))
```
