import com.banno._

resolvers ++= Seq(
  "Sonatype Staging Repo" at "https://oss.sonatype.org/content/repositories/staging"
)

Specs2.settings

libraryDependencies ++= Seq(
  "com.zaxxer" % "HikariCP-java6" % "2.3.0-rc3",
  "org.squeryl" %% "squeryl" % "0.9.5-6",
  "org.postgresql" % "postgresql" % "9.3-1102-jdbc41"
)
