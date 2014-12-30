import com.banno._

lazy val root = bannoRootProject("data-root").aggregate(common, config, actors, httpClient, httpServer, postgres)

lazy val common = bannoProject("common", "data-common")

lazy val config = bannoProject("config", "data-config").dependsOn(common)

lazy val actors = bannoProject("actors", "data-actors").dependsOn(common, config)

lazy val httpClient = bannoProject("http", "data-http-client", file("./http/client")).dependsOn(common, config)

lazy val httpServer = bannoProject("http", "data-http-server", file("./http/server")).dependsOn(common, config)

lazy val postgres = bannoProject("postgres", "data-postgres").dependsOn(common, config)

scalaVersion := "2.11.4"
