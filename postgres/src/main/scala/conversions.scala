package data.postgres
import java.sql.Timestamp
import org.joda.time.DateTime
import scala.language.implicitConversions

object SquerylConversions {
  object time {
    implicit def DateTimeToTimestamp(dt: DateTime): Timestamp = new Timestamp(dt.toDate.getTime)
    implicit def TimestampToDateTime(ts: Timestamp): DateTime = new DateTime(ts.getTime)
  }
}
