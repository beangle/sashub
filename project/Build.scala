import org.beangle.parent.Dependencies.*
import sbt.*

object SasDepends {
  val beangle_commons = "org.beangle.commons" % "beangle-commons" % "6.1.0"
  val beangle_jdbc = "org.beangle.jdbc" % "beangle-jdbc" % "1.1.8"
  val beangle_data_model = "org.beangle.data" % "beangle-data-model" % "5.12.2"
  val beangle_data_hibernate = "org.beangle.data" % "beangle-data-hibernate" % "5.12.2"
  val beangle_cdi = "org.beangle.cdi" % "beangle-cdi" % "0.10.3"
  val beangle_cache = "org.beangle.cache" % "beangle-cache" % "0.1.19"
  val beangle_template = "org.beangle.template" % "beangle-template" % "0.2.6"
  val beangle_web = "org.beangle.web" % "beangle-web" % "0.7.7"
  val beangle_webmvc = "org.beangle.webmvc" % "beangle-webmvc" % "0.14.5"
  val beangle_bui_bootstrap = "org.beangle.bui" % "beangle-bui-bootstrap" % "0.1.5"
  val beangle_serializer = "org.beangle.serializer" % "beangle-serializer" % "0.1.25"
  val beangle_security = "org.beangle.security" % "beangle-security" % "4.4.14"
  val beangle_ids = "org.beangle.ids" % "beangle-ids" % "0.4.12"
  val beangle_event = "org.beangle.event" % "beangle-event" % "0.1.7"
  val beangle_transfer = "org.beangle.transfer" % "beangle-transfer" % "0.0.6"
  val beangle_ems_app = "org.beangle.ems" % "beangle-ems-app" % "4.18.9"
  val beangle_she = "org.beangle.she" % "beangle-she" % "0.0.8"

  val caffeine_jcache = "com.github.ben-manes.caffeine" % "jcache" % "3.2.2" exclude("org.osgi", "org.osgi.service.component.annotations") exclude("javax.inject", "javax.inject")

  val appDepends = Seq(beangle_commons, logback_classic, logback_core, scalatest, beangle_web, beangle_bui_bootstrap, HikariCP) ++
    Seq(beangle_data_hibernate, hibernate_core, beangle_jdbc, beangle_cache, beangle_security, beangle_template, hibernate_jcache) ++
    Seq(postgresql, beangle_ems_app, beangle_serializer, caffeine_jcache, hibernate_jcache, freemarker, beangle_she)
}
