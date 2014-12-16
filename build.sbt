import com.banno._

lazy val root = bannoRootProject("data-root").aggregate(common, http, postgres)

lazy val common = bannoProject("common", "data-common")

lazy val http = bannoProject("http", "data-http").dependsOn(common)

lazy val postgres = bannoProject("postgres", "data-postgres").dependsOn(common)
