package data.postgres
import data.common.Logging
import data.config.Config
import org.squeryl.Session
import java.sql.SQLException
import org.postgresql.util.PSQLException
import org.apache.commons.io.IOUtils

trait SchemaMigration extends PostgresDriver with Logging {
  protected def migrationFolder: String
  def migrate(): Unit

  def run(filename: String): Unit = {
    if (Config.default.getBoolean("postgres.migrations.enabled")) {
      val rawSql = readFile(s"/${migrationFolder}/${filename}")
      withSession {
        val currentSession = Session.currentSession
        val statement = currentSession.connection.createStatement
        try {
          statement.execute(rawSql)
        } catch {
          case e: PSQLException =>
            log.error(s"Unable to execute sql of: \n\n${rawSql}\n\n", e)
            throw e
          case e: SQLException =>
            log.error(s"Unable to execute sql of: \n\n${rawSql}\n\n", e)
            throw e
        }
      }
    } else {
      log.debug(s"Postgres migrations are disabled.")
    }
  }

  private[this] def readFile(path: String): String =
    try {
      IOUtils.toString(getClass().getResourceAsStream(path))
    } catch {
      case err: NullPointerException =>
        throw new RuntimeException(s"Unable to load raw sql file at: ${path}")
    }
}
