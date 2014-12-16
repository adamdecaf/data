import com.banno._

name := "data-common"

BannoSettings.settings

resolvers += DefaultMavenRepository

libraryDependencies ++= Seq(
  "org.joda" % "joda-convert" % "1.6",
  "joda-time" % "joda-time" % "2.4",
  "org.apache.commons" % "commons-io" % "1.3.2"
)
