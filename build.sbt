import com.banno._

lazy val root = Project("data-root", file(".")).aggregate(actors, aws, crawler, common, config, httpClient, httpServer, postgres)

lazy val common = Project("common", file("./common"))

lazy val config = Project("config", file("./config")).dependsOn(common)

lazy val actors = Project("actors", file("./actors")).dependsOn(common, config)

lazy val aws = Project("aws", file("./aws")).dependsOn(common, config)

lazy val httpClient = Project("http", file("./http/client")).dependsOn(common, config, actors)

lazy val httpServer = Project("http", file("./http/server")).dependsOn(common, config)

lazy val postgres = Project("postgres", file("./postgres")).dependsOn(common, config)

// deplyables

lazy val crawler = Project("crawler", file("./crawler")).dependsOn(actors, config, common, httpClient, postgres)

lazy val extractor = Project("extractor", file("./extractor")).dependsOn(actors, aws, config, common, postgres)

scalaVersion := "2.11.4"

BannoPrompt.settings
