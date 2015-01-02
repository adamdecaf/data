package data.http
import data.common.UUID
import java.sql.Timestamp
import org.joda.time.DateTime
import org.specs2.specification.Scope
import org.specs2.mutable.Specification

object HttpStorageRepositorySpec extends Specification {

  "read and write" in new context {
    getHttpExchange(httpExchangeId) must beNone
    getHttpRequest(httpRequestId) must beNone
    getHttpResponse(httpResponseId) must beNone

    writeHttpRequest(req)
    getHttpExchange(httpExchangeId) must beNone
    getHttpRequest(httpRequestId) must beSome(req)

    writeHttpResponse(res)
    getHttpExchange(httpExchangeId) must beSome(HttpExchange(httpExchangeId, req, res))
    getHttpRequest(httpRequestId) must beSome(req)

    // missing, wrong on purpose
    getHttpExchange(httpRequestId) must beNone
    getHttpRequest(httpResponseId) must beNone
    getHttpResponse(httpRequestId) must beNone
  }

  trait context extends Scope with PostgresHttpStorageRepository {
    val httpExchangeId = UUID()
    val httpRequestId = UUID()
    val httpResponseId = UUID()
    val req = HttpRequest(httpRequestId, httpExchangeId, "uri", DateTime.now)
    val res = HttpResponse(httpResponseId, httpExchangeId, "uri", 200, "s3", DateTime.now)
  }
}
