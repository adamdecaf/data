import com.banno._

lazy val root = Project("data-root", file(".")).aggregate(actors, aws, crawler, common, config, httpClient, httpServer, httpStorge, postgres)

// libraries

lazy val common = Project("common", file("./common"))

lazy val config = Project("config", file("./config")).dependsOn(common)

lazy val actors = Project("actors", file("./actors")).dependsOn(common, config)

lazy val aws = Project("aws", file("./aws")).dependsOn(common, config)

// http

lazy val httpClient = Project("http-client", file("./http/client")).dependsOn(actors, common, config, httpServer)

lazy val httpServer = Project("http-server", file("./http/server")).dependsOn(actors, common, config, httpStorge)

lazy val httpStorge = Project("http-storage", file("./http/storage")).dependsOn(common, config, postgres)

// storage

lazy val postgres = Project("postgres", file("./postgres")).dependsOn(common, config)

// deplyables

lazy val crawler = Project("crawler", file("./crawler")).dependsOn(actors, config, common, httpClient, httpStorge, postgres)

lazy val extractor = Project("extractor", file("./extractor")).dependsOn(actors, aws, config, common, postgres)

scalaVersion := "2.11.4"

BannoPrompt.settings
