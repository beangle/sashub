import org.beangle.parent.Dependencies.*
import sbt.*

object SasDepends {
  val b_commons = "org.beangle.commons" % "beangle-commons" % "5.6.29"
  val b_jdbc = "org.beangle.jdbc" % "beangle-jdbc" % "1.0.11"
  val b_model = "org.beangle.data" % "beangle-model" % "5.8.24"
  val b_cdi = "org.beangle.cdi" % "beangle-cdi" % "0.7.3"
  val b_cache = "org.beangle.cache" % "beangle-cache" % "0.1.14"
  val b_template = "org.beangle.template" % "beangle-template" % "0.1.26"
  val b_web = "org.beangle.web" % "beangle-web" % "0.6.4"
  val b_webmvc = "org.beangle.webmvc" % "beangle-webmvc" % "0.10.6"
  val b_bui_bootstrap = "org.beangle.bui" % "beangle-bui-bootstrap" % "0.0.6"
  val b_serializer = "org.beangle.serializer" % "beangle-serializer" % "0.1.18"
  val b_security = "org.beangle.security" % "beangle-security" % "4.3.29"
  val b_ids = "org.beangle.ids" % "beangle-ids" % "0.3.24"
  val b_event = "org.beangle.event" % "beangle-event" % "0.1.0"
  val b_doc_transfer = "org.beangle.doc" % "beangle-doc-transfer" % "0.4.14"
  val b_ems_app = "org.beangle.ems" % "beangle-ems-app" % "4.11.3"

  val caffeine_jcache = "com.github.ben-manes.caffeine" % "jcache" % "3.2.1" exclude("org.osgi", "org.osgi.service.component.annotations") exclude("javax.inject", "javax.inject")

  val appDepends = Seq(b_commons, logback_classic, logback_core, scalatest, b_web, b_bui_bootstrap, HikariCP) ++
    Seq(b_model, hibernate_core, b_jdbc, b_cache, b_security, b_template, hibernate_jcache) ++
    Seq(postgresql, b_ems_app, b_serializer, caffeine_jcache, hibernate_jcache, freemarker)
}
