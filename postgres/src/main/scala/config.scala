package data.postgres

trait PostgresConfig {
  def jdbcUrl: String
  def postgresUsername: String
  def postgresPassword: String
  def loginTimeout: Int
  def connectionTimeout: Long
  def maxPoolSize: Int
}

object PostgresConfig {
  import data.config.Config.{default => config}

  lazy val default = new PostgresConfig {
    lazy val jdbcUrl: String          = config.getString("postgres.jdbc-url")
    lazy val postgresUsername: String = config.getString("postgres.username")
    lazy val postgresPassword: String = config.getString("postgres.password")
    lazy val loginTimeout: Int        = config.getInt("postgres.login-timeout")
    lazy val connectionTimeout: Long  = config.getLong("postgres.connection-timeout")
    lazy val maxPoolSize: Int         = config.getInt("postgres.max-pool-size")
  }
}
