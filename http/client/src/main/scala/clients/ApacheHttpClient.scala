package data.http
import data.config.Config
import java.nio.charset.Charset
import org.apache.http.config.{ConnectionConfig, SocketConfig}
import org.apache.http.client.HttpClient
import org.apache.http.client.config.RequestConfig
import org.apache.http.impl.client.cache.CachingHttpClient
import org.apache.http.impl.client.{DecompressingHttpClient, HttpClientBuilder}
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager

object ApacheHttpClient {
  private[this] val config = Config.default

  lazy val httpClient: HttpClient = {
    val builder = HttpClientBuilder.create()

    builder.setConnectionManager {
      val manager = new PoolingHttpClientConnectionManager()
      manager.setMaxTotal(config.getInt("http.client.max-total-connections"))
      manager.setDefaultMaxPerRoute(config.getInt("http.client.max-connections-per-route"))
      manager
    }

    builder.setDefaultConnectionConfig {
      val config = ConnectionConfig.custom()
      val charset = Charset.forName("UTF-8")
      config.setCharset(charset)
      config.build()
    }

    builder.setUserAgent(config.getString("http.client.user-agent"))

    val requestConfig = RequestConfig
      .custom
      .setConnectionRequestTimeout(config.getDuration("http.client.timeouts.connection-request-timeout").toMillis.toInt)
      .setConnectTimeout(config.getDuration("http.client.timeouts.connect-timeout").toMillis.toInt)
      .setSocketTimeout(config.getDuration("http.client.timeouts.socket-timeout").toMillis.toInt)
      .setExpectContinueEnabled(false)
      .setCircularRedirectsAllowed(true)
      .build
    builder.setDefaultRequestConfig(requestConfig)

    val socketConfig = SocketConfig
      .custom
      .setSoTimeout(config.getDuration("http.client.timeouts.socket-timeout").toMillis.toInt)
      .setTcpNoDelay(true)
      .build
    builder.setDefaultSocketConfig(socketConfig)

    builder.build()
  }
}
