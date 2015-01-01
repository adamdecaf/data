import com.banno._

lazy val root = bannoRootProject("data-root").aggregate(common, config, actors, aws, httpClient, httpServer, postgres, crawler)

lazy val common = bannoProject("common", "data-common")

lazy val config = bannoProject("config", "data-config").dependsOn(common)

lazy val actors = bannoProject("actors", "data-actors").dependsOn(common, config)

lazy val aws = bannoProject("aws", "data-aws").dependsOn(common, config)

lazy val httpClient = bannoProject("http", "data-http-client", file("./http/client")).dependsOn(common, config, actors)

lazy val httpServer = bannoProject("http", "data-http-server", file("./http/server")).dependsOn(common, config)

lazy val postgres = bannoProject("postgres", "data-postgres").dependsOn(common, config)

// deplyables

lazy val crawler = bannoProject("crawler", "data-crawler").dependsOn(actors, config, common, httpClient, postgres)

scalaVersion := "2.11.4"
