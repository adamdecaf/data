import com.banno._

name := "data-common"

BannoSettings.settings

resolvers += DefaultMavenRepository

Specs2.settings

libraryDependencies ++= Seq(
  "org.joda" % "joda-convert" % "1.6",
  "joda-time" % "joda-time" % "2.4"
)
