package data.http
import scala.concurrent.Future
import org.apache.http.client.methods.{HttpGet, HttpPost}

trait HttpRequestBuilder {
  def addHeader(key: String, value: String): HttpRequestBuilder
  def execute(): Future[HttpResponse]
}

object HttpRequestBuilder {
  def GET(uri: String): HttpRequestBuilder = new ApacheHttpClientRequestBuilder(new HttpGet(uri))
  def POST(uri: String): HttpRequestBuilder = new ApacheHttpClientRequestBuilder(new HttpGet(uri))
}
