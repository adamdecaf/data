import com.banno._

lazy val root = bannoRootProject("data-root").aggregate(common, config, http, postgres)

lazy val common = bannoProject("common", "data-common")

lazy val config = bannoProject("config", "data-config").dependsOn(common)

lazy val http = bannoProject("http", "data-http").dependsOn(common, config)

lazy val postgres = bannoProject("postgres", "data-postgres").dependsOn(common, config)
