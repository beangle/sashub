import org.beangle.parent.Dependencies.*
import sbt.*

object SasDepends {
  val beangle_commons = "org.beangle.commons" % "beangle-commons" % "5.6.30"
  val beangle_jdbc = "org.beangle.jdbc" % "beangle-jdbc" % "1.0.12"
  val beangle_model = "org.beangle.data" % "beangle-model" % "5.9.0"
  val beangle_cdi = "org.beangle.cdi" % "beangle-cdi" % "0.7.4"
  val beangle_cache = "org.beangle.cache" % "beangle-cache" % "0.1.15"
  val beangle_template = "org.beangle.template" % "beangle-template" % "0.1.27"
  val beangle_web = "org.beangle.web" % "beangle-web" % "0.6.5"
  val beangle_webmvc = "org.beangle.webmvc" % "beangle-webmvc" % "0.10.7"
  val beangle_bui_bootstrap = "org.beangle.bui" % "beangle-bui-bootstrap" % "0.0.7"
  val beangle_serializer = "org.beangle.serializer" % "beangle-serializer" % "0.1.19"
  val beangle_security = "org.beangle.security" % "beangle-security" % "4.3.30"
  val beangle_ids = "org.beangle.ids" % "beangle-ids" % "0.3.25"
  val beangle_event = "org.beangle.event" % "beangle-event" % "0.1.1"
  val beangle_doc_transfer = "org.beangle.doc" % "beangle-doc-transfer" % "0.4.15"
  val beangle_ems_app = "org.beangle.ems" % "beangle-ems-app" % "4.11.4"

  val caffeine_jcache = "com.github.ben-manes.caffeine" % "jcache" % "3.2.2" exclude("org.osgi", "org.osgi.service.component.annotations") exclude("javax.inject", "javax.inject")

  val appDepends = Seq(beangle_commons, logback_classic, logback_core, scalatest, beangle_web, beangle_bui_bootstrap, HikariCP) ++
    Seq(beangle_model, hibernate_core, beangle_jdbc, beangle_cache, beangle_security, beangle_template, hibernate_jcache) ++
    Seq(postgresql, beangle_ems_app, beangle_serializer, caffeine_jcache, hibernate_jcache, freemarker)
}
