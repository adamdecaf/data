package data.http
import data.actors.BlockingExecutionContext
import data.config.Config
import data.common.{Logging, UUID}
import scala.concurrent.Future
import scala.collection.convert.WrapAsScala
import java.util.NoSuchElementException
import java.lang.IllegalArgumentException
import java.net.UnknownHostException
import javax.net.ssl.{SSLHandshakeException, SSLPeerUnverifiedException}
import org.apache.http.conn.HttpHostConnectException
import org.apache.http.impl.client.CloseableHttpClient
import org.apache.http.client.methods.HttpUriRequest

final class ApacheHttpClientRequestBuilder(request: HttpUriRequest) extends HttpRequestBuilder {

  final def addHeader(key: String, value: String): HttpRequestBuilder = {
    request.addHeader(key, value)
    new ApacheHttpClientRequestBuilder(request)
  }

  final def execute(): Future[HttpResponse] = ApacheHttpClientRequestBuilder.execute(request)
}

private object ApacheHttpClientRequestBuilder extends BlockingExecutionContext with Logging {
  private[this] val config = Config.default

  def execute(request: HttpUriRequest): Future[HttpResponse] = {
    val maxRetries = config.getInt("http.client.max-retries")
    def attempt(tries: Int = 0): Future[HttpResponse] = {
      if (tries >= maxRetries) {
        Future.failed(TooManyRetriesException)
      } else {

        try {
          implicit val ec = executionContext

          Future {
            val response = ApacheHttpClient.httpClient.execute(request)
            val data = response.getEntity().getContent()
            val contentLength = response.getEntity().getContentLength()
            val statusCode = response.getStatusLine().getStatusCode()

            val filename = UUID()
            val tempDir = Option(System.getProperty("java.io.tmpdir")).getOrElse("/tmp")
            val basePath = config.getString("http.client.responses.base-dir")

            val fullFilePath = {
              val prefix = if (tempDir.endsWith("/")) {
                if (basePath.startsWith("/")) {
                  tempDir + basePath.drop(1)
                } else {
                  tempDir + basePath
                }
              } else {
                if (basePath.startsWith("/")) {
                  tempDir + "/" + basePath
                } else {
                  tempDir + basePath
                }
              }
              prefix + filename
            }

            // write response to file
            try {
              log.debug(s"Writing response to ${fullFilePath}")
              FileWriting.writeInputStreamToFile(data, fullFilePath)
            } finally {
              data.close()
            }

            val headers: Map[String, String] = {
              val list = response.getAllHeaders().toList
              list map { h => (h.getName -> h.getValue) } toMap
            }

            HttpResponse(
              dataFilepath = fullFilePath,
              contentLength = contentLength,
              statusCode = statusCode,
              headers = headers
            )
          }

        } catch {
          case err: SSLPeerUnverifiedException =>
            log.debug(s"Retrying request due to SSLPeerUnverifiedException")
            attempt(tries + 1)

          case err: SSLHandshakeException =>
            log.debug(s"Retrying request due to SSLHandshakeException")
            attempt(tries + 1)

          case err: HttpHostConnectException =>
            log.debug(s"Retrying request due to HttpHostConnectException")
            attempt(tries + 1)

          case err: UnknownHostException     => Future.failed(err)
          case err: NoSuchElementException   => Future.failed(err)
          case err: IllegalArgumentException => Future.failed(err)
        }

      }
    }

    attempt()
  }
}
