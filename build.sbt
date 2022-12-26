import org.beangle.parent.Settings._
import SasDepends._

ThisBuild / organization := "org.beangle.sashub"
ThisBuild / version := "0.0.1-SNAPSHOT"

ThisBuild / scmInfo := Some(
  ScmInfo(
    url("https://github.com/beangle/sashub"),
    "scm:git@github.com:beangle/sashub.git"
  )
)

ThisBuild / developers := List(
  Developer(
    id    = "chaostone",
    name  = "Tihua Duan",
    email = "duantihua@gmail.com",
    url   = url("http://github.com/duantihua")
  )
)

ThisBuild / description := "The Beangle Sas Admin"
ThisBuild / homepage := Some(url("http://beangle.github.io/sashub/index.html"))
ThisBuild / resolvers += Resolver.mavenLocal

lazy val root = (project in file("."))
  .settings()
  .aggregate(webapp)

lazy val webapp = (project in file("webapp"))
  .enablePlugins(WarPlugin,TomcatPlugin)
  .settings(
    name := "beangle-sashub-webapp",
    common,
    crossPaths := false,
    libraryDependencies ++= appDepends
  )

publish / skip := true
