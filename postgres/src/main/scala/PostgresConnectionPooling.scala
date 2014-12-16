package data.postgres
import data.config.Config
import java.sql.Connection
import com.zaxxer.hikari.HikariDataSource
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicReference

object PostgresConnectionPooling {
  private[this] val dataSource = new AtomicReference[HikariDataSource]()

  // todo: shutdown hook to close/shutdown data source
  // todo: HikariDataSource.setConnectionTestQuery()
  // todo: public void setMetricRegistry(Object metricRegistry)

  final def initialize(config: PostgresConfig): Unit = {
    Class.forName("org.postgresql.Driver")
    val hikariDataSource = {
      val source = new HikariDataSource()
      source.setJdbcUrl(config.jdbcUrl)
      source.setLoginTimeout(config.loginTimeout)
      source.setConnectionTimeout(config.connectionTimeout)
      source.setMaximumPoolSize(config.maxPoolSize)
      source.setPassword(config.postgresPassword)
      source.setUsername(config.postgresUsername)

      // check that the pool is valid
      source.validate()
      source
    }

    dataSource.set(hikariDataSource)
  }

  final def getConnection(): Connection = {
    val max = Config.default.getInt("postgres.session-retry-max")

    @annotation.tailrec
    def retry(count: Int = max): Connection = {
      if (count <= 0) {
        throw new RuntimeException("Failed to get connection to pool, blowing up.")
      } else {
        val source = dataSource.get()
        if (source == null) {
          initialize(PostgresConfig.default)
          retry(count - 1)
        } else {
          source.getConnection()
        }
      }
    }

    retry()
  }
}
