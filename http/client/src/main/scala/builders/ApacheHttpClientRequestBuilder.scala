package data.http
import scala.concurrent.Future

final class ApacheHttpClientRequestBuilder() extends HttpRequestBuilder {
  def addHeader(key: String, value: String): HttpRequestBuilder = ???
  def execute(): Future[HttpResponse] = ???
  def build(): HttpRequest = ???
}
