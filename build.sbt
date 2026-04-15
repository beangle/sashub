import SasDepends.*
import org.beangle.parent.Settings.*

ThisBuild / organization := "org.beangle.sashub"
ThisBuild / version := "0.0.6"

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
  .enablePlugins(WarPlugin, TomcatPlugin)
  .settings(
    name := "beangle-sashub",
    common,
    libraryDependencies ++= appDepends,
    crossPaths := false
  )

