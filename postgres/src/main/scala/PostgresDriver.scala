package data.postgres
import java.sql.Connection
import org.squeryl.{PrimitiveTypeMode, Session}
import org.squeryl.adapters.PostgreSqlAdapter

trait PostgresDriver {
  final def findSession: Session = {
    val connection = PostgresConnectionPooling.getConnection()
    Session.create(connection, new PostgreSqlAdapter)
  }

  final def withSession[T](body: => T): T = {
    if (Session.hasCurrentSession) {
      PrimitiveTypeMode.inTransaction(body)
    } else {
      PrimitiveTypeMode.transaction(findSession)(body)
    }
  }
}
