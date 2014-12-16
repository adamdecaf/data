import com.banno._

name := "data-postgres"

libraryDependencies ++= Seq(
  "com.zaxxer" % "HikariCP-java6" % "2.2.5",
  "org.squeryl" %% "squeryl" % "0.9.5-6",
  "org.postgresql" % "postgresql" % "9.3-1102-jdbc41"
)
