import org.beangle.parent.Dependencies._
import sbt._

object SasDepends {
  val commonsVer = "5.6.10"
  val dataVer = "5.8.0"
  val cdiVer = "0.6.2"
  val webVer = "0.4.10"
  val serializerVer = "0.1.8"
  val cacheVer = "0.1.7"
  val templateVer = "0.1.10"
  val webmvcVer = "0.9.23"
  val securityVer = "4.3.16"
  val idsVer = "0.3.15"
  val emsVer = "4.8.7"

  val commonsCore = "org.beangle.commons" %% "beangle-commons-core" % commonsVer
  val commonsFile = "org.beangle.commons" %% "beangle-commons-file" % commonsVer
  val dataJdbc = "org.beangle.data" %% "beangle-data-jdbc" % dataVer
  val dataOrm = "org.beangle.data" %% "beangle-data-orm" % dataVer
  val dataTransfer = "org.beangle.data" %% "beangle-data-transfer" % dataVer
  val cdiApi = "org.beangle.cdi" %% "beangle-cdi-api" % cdiVer
  val cdiSpring = "org.beangle.cdi" %% "beangle-cdi-spring" % cdiVer
  val cacheApi = "org.beangle.cache" %% "beangle-cache-api" % cacheVer
  val cacheCaffeine = "org.beangle.cache" %% "beangle-cache-caffeine" % cacheVer
  val templateApi = "org.beangle.template" %% "beangle-template-api" % templateVer
  val templateFreemarker = "org.beangle.template" %% "beangle-template-freemarker" % templateVer
  val webAction = "org.beangle.web" %% "beangle-web-action" % webVer
  val webServlet = "org.beangle.web" %% "beangle-web-servlet" % webVer
  val webmvcCore = "org.beangle.webmvc" %% "beangle-webmvc-core" % webmvcVer
  val webmvcView = "org.beangle.webmvc" %% "beangle-webmvc-view" % webmvcVer
  val webmvcSupport = "org.beangle.webmvc" %% "beangle-webmvc-support" % webmvcVer
  val serializerText = "org.beangle.serializer" %% "beangle-serializer-text" % serializerVer
  val securityCore = "org.beangle.security" %% "beangle-security-core" % securityVer
  val securityWeb = "org.beangle.security" %% "beangle-security-web" % securityVer
  val securitySession = "org.beangle.security" %% "beangle-security-session" % securityVer
  val securitySso = "org.beangle.security" %% "beangle-security-sso" % securityVer
  val idsCas = "org.beangle.ids" %% "beangle-ids-cas" % idsVer
  val idsWeb = "org.beangle.ids" %% "beangle-ids-web" % idsVer
  val emsApp = "org.beangle.ems" %% "beangle-ems-app" % emsVer

  val caffeine_jcache = "com.github.ben-manes.caffeine" % "jcache" % "3.1.8" exclude("org.osgi", "org.osgi.service.component.annotations") exclude("javax.inject", "javax.inject")

  val appDepends = Seq(commonsCore, commonsFile, logback_classic, logback_core, scalatest, webAction, cdiApi, gson, HikariCP) ++
    Seq(dataOrm, hibernate_core, dataJdbc, cacheApi, cacheCaffeine, securitySession, securitySso, templateApi, hibernate_jcache) ++
    Seq(caffeine_jcache, postgresql, emsApp, webmvcSupport, webmvcView, serializerText)
}
