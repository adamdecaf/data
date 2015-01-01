package data.aws
import data.config.Config
import com.amazonaws.ClientConfiguration
import com.amazonaws.auth.BasicAWSCredentials

object AWSConfig {
  private[this] val config = Config.default

  final lazy val accessKey: String =
    Option(System.getenv("AWS_ACCESS_KEY_ID")).getOrElse(config.getString("aws.access-key"))

  final lazy val secretAccessKey: String =
    Option(System.getenv("AWS_SECRET_ACCESS_KEY")).getOrElse(config.getString("aws.secret-access-key"))

  lazy val awsConfig = new ClientConfiguration()
    .withConnectionTimeout(timeout("aws.connection-timeout"))
    .withMaxConnections(config.getInt("aws.max-connections"))
    .withMaxErrorRetry(config.getInt("aws.max-error-retry"))
    .withSocketTimeout(timeout("aws.socket-timeout"))

  lazy val awsCredentials = new BasicAWSCredentials(accessKey, secretAccessKey)

  private[this] def timeout(key: String): Int = config.getDuration(key).toMillis.toInt
}
