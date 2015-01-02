package data.http
import data.postgres.{PostgresDriver, SquerylConversions}
import org.squeryl.PrimitiveTypeMode

trait HttpStorageRepository {
  def getHttpExchange(httpExchangeId: String): Option[HttpExchange]
  def getHttpRequest(httpRequestId: String): Option[HttpRequest]
  def getHttpResponse(httpResponseId: String): Option[HttpResponse]
  def writeHttpRequest(req: HttpRequest): Unit
  def writeHttpResponse(req: HttpResponse): Unit
}

trait PostgresHttpStorageRepository extends HttpStorageRepository with PrimitiveTypeMode {
  import SquerylConversions.time._

  def getHttpExchange(httpExchangeId: String): Option[HttpExchange] = {
    def lookupRequest(): Option[HttpRequest] =
      httpStorageDriver.withSession {
        from(HttpStorageSchema.requests)(h =>
          where(h.httpExchangeId === httpExchangeId)
          select(h)).page(0,1).headOption
      }

    def lookupResponse(): Option[HttpResponse] =
      httpStorageDriver.withSession {
        from(HttpStorageSchema.responses)(h =>
          where(h.httpExchangeId === httpExchangeId)
          select(h)).page(0,1).headOption
      }

    for {
      req <- lookupRequest()
      res <- lookupResponse()
    } yield HttpExchange(httpExchangeId, req, res)
  }

  def getHttpRequest(httpRequestId: String): Option[HttpRequest] =
    httpStorageDriver.withSession {
      from(HttpStorageSchema.requests)(h =>
        where(h.httpRequestId === httpRequestId)
        select(h)).page(0,1).headOption
    }

  def getHttpResponse(httpResponseId: String): Option[HttpResponse] =
    httpStorageDriver.withSession {
      from(HttpStorageSchema.responses)(h =>
        where(h.httpResponseId === httpResponseId)
        select(h)).page(0,1).headOption
    }

  def writeHttpRequest(req: HttpRequest): Unit =
    httpStorageDriver.withSession {
      HttpStorageSchema.requests.insert(req)
    }

  def writeHttpResponse(res: HttpResponse): Unit =
    httpStorageDriver.withSession {
      HttpStorageSchema.responses.insert(res)
    }

  protected lazy val httpStorageDriver = new PostgresDriver
}
