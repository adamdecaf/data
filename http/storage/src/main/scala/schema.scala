package data.http
import java.sql.Timestamp
import org.joda.time.DateTime
import org.squeryl._
import org.squeryl.dsl._
import org.squeryl.annotations.Column

object HttpStorageSchema extends PrimitiveTypeMode {
  import scala.language.implicitConversions
  object Tables extends Schema with BaseQueryDsl {
    val requests = table[HttpRequest]("http_requests")
    val responses = table[HttpResponse]("http_responses")
  }

  // Taken from: http://squeryl.org/0.9.6.html
  implicit val jodaTimeTEF = new NonPrimitiveJdbcMapper[Timestamp, DateTime, TTimestamp](timestampTEF, this) {
    def convertFromJdbc(t: Timestamp) = new DateTime(t)
    def convertToJdbc(t: DateTime) = new Timestamp(t.getMillis())
  }

  implicit val optionJodaTimeTEF =
    new TypedExpressionFactory[Option[DateTime], TOptionTimestamp] with DeOptionizer[Timestamp, DateTime, TTimestamp, Option[DateTime], TOptionTimestamp] {
      val deOptionizer = jodaTimeTEF
    }

  implicit def jodaTimeToTE(s: DateTime) = jodaTimeTEF.create(s)
  implicit def optionJodaTimeToTE(s: Option[DateTime]) = optionJodaTimeTEF.create(s)
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
