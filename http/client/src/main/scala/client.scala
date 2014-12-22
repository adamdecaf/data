package data.http
import data.config.Config
import scala.concurrent.Future

trait HttpClient {
  def httpClient: Http

  trait Http {
    def GET: HttpRequestBuilder
  }
}
