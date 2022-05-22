import org.beangle.parent.Settings._
import SasDepends._

ThisBuild / organization := "org.beangle.sasadmin"
ThisBuild / version := "0.0.1-SNAPSHOT"

ThisBuild / scmInfo := Some(
  ScmInfo(
    url("https://github.com/beangle/sasadmin"),
    "scm:git@github.com:beangle/ems.git"
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
ThisBuild / homepage := Some(url("http://beangle.github.io/sasadmin/index.html"))
ThisBuild / resolvers += Resolver.mavenLocal

lazy val root = (project in file("."))
  .settings()
  .aggregate(webapp)

lazy val webapp = (project in file("webapp"))
  .enablePlugins(WarPlugin,UndertowPlugin)
  .settings(
    name := "beangle-sasadmin-webapp",
    common,
    crossPaths := false,
    libraryDependencies ++= appDepends
  )

publish / skip := true
