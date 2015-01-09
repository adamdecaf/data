import com.banno._

Akka.settings

Metrics.settings

Specs2.settings

libraryDependencies <++= (Metrics.version) { m =>
  Seq(
    "com.codahale.metrics" % "metrics-logback" % m,
    "com.codahale.metrics" % "metrics-jvm" % m,
    "com.codahale.metrics" % "metrics-servlets" % m
  )
}
