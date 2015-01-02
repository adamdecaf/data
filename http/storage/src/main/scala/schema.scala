package data.http
import org.joda.time.DateTime
import org.squeryl.annotations.Column
import org.squeryl.{Table, Schema, KeyedEntity}

object HttpStorageSchema extends Schema {
  val requests = table[HttpRequest]("http_requests")
  val responses = table[HttpResponse]("http_responses")
}

case class HttpExchange(
  httpExchangeId: String,
  request: HttpRequest,
  response: HttpResponse
)

case class HttpRequest(
  @Column("http_request_id") httpRequestId: String,
  @Column("http_exchange_id") httpExchangeId: String,
  uri: String,
  time: DateTime
) extends KeyedEntity[String] {
  def id = httpRequestId
  def this() = this("", "", "", DateTime.now)
}

case class HttpResponse(
  @Column("http_response_id") httpResponseId: String,
  @Column("http_exchange_id") httpExchangeId: String,
  uri: String,
  @Column("status_code") statusCode: Int,
  @Column("s3_object_key") s3ObjectKey: String,
  time: DateTime
) extends KeyedEntity[String] {
  def id = httpResponseId
}
