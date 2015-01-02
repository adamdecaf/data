import com.banno._

resolvers += DefaultMavenRepository

libraryDependencies ++= Seq(
  "org.joda" % "joda-convert" % "1.6",
  "joda-time" % "joda-time" % "2.4",
  "org.slf4j" % "slf4j-api" % "1.7.5",
  "org.slf4j" % "log4j-over-slf4j" % "1.7.5",
  "org.slf4j" % "jcl-over-slf4j" % "1.7.5",
  "org.apache.commons" % "commons-io" % "1.3.2"
)
