import SasDepends.*
import org.beangle.parent.Settings.*

ThisBuild / organization := "org.beangle.sashub"
ThisBuild / version := "0.0.6-SNAPSHOT"

ThisBuild / scmInfo := Some(
  ScmInfo(
    url("https://github.com/beangle/sashub"),
    "scm:git@github.com:beangle/sashub.git"
  )
)

ThisBuild / developers := List(
  Developer(
    id = "chaostone",
    name = "Tihua Duan",
    email = "duantihua@gmail.com",
    url = url("http://github.com/duantihua")
  )
)

ThisBuild / description := "The Beangle Sas Hub"
ThisBuild / homepage := Some(url("http://beangle.github.io/sashub/index.html"))
ThisBuild / resolvers += Resolver.mavenLocal

lazy val root = (project in file("."))
  .settings()
  .aggregate(core, webapp)

lazy val core = (project in file("core"))
  .settings(
    name := "beangle-sashub-core",
    common,
    crossPaths := false,
    libraryDependencies ++= appDepends
  )

lazy val webapp = (project in file("webapp"))
  .enablePlugins(WarPlugin, TomcatPlugin)
  .settings(
    name := "beangle-sashub-webapp",
    common,
    crossPaths := false
  ).dependsOn(core)

publish / skip := true
