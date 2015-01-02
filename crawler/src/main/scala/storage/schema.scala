package data.crawler
import java.sql.Timestamp
import org.squeryl.annotations.Column
import org.squeryl.{Table, Schema, KeyedEntity}
// import org.squeryl.PrimitiveTypeMode._

object HttpStorageSchema extends Schema {
  val requests = table[HttpRequestsRow]("http_requests")
  val responses = table[HttpResponsesRow]("http_responses")
}

case class HttpRequestsRow(
  http_request_id: String,
  http_exchange_id: String,
  uri: String,
  time: Timestamp
)

case class HttpResponsesRow(
  http_response_id: String,
  http_exchange_id: String,
  uri: String,
  status_code: Int,
  s3_object_key: String,
  time: Timestamp
)
