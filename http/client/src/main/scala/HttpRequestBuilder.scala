package data.http
import scala.concurrent.Future

trait HttpRequestBuilder {
  def addHeader(key: String, value: String): HttpRequestBuilder
  def execute(): Future[HttpResponse]
  def build(): HttpRequest
}

object HttpRequestBuilder {
  def empty: HttpRequestBuilder = new ApacheHttpClientRequestBuilder()
}
