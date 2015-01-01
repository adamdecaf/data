package data.config
import com.typesafe.config.ConfigFactory
import scala.concurrent.duration.Duration

object Config {
  lazy val default = new Config()
}

final class Config {
  private[this] val underlying = ConfigFactory.load()

  final def getString(key: String): String = underlying.getString(key)
  final def getInt(key: String): Int = underlying.getInt(key)
  final def getLong(key: String): Long = underlying.getLong(key)
  final def getBoolean(key: String): Boolean = underlying.getBoolean(key)
  final def getDuration(key: String): Duration = Duration(getString(key))
}
