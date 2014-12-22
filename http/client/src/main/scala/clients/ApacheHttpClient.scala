package data.http
import data.config.Config

// import org.apache.http.HttpEntity
// import org.apache.http.NameValuePair
// import org.apache.http.client.entity.UrlEncodedFormEntity
// import org.apache.http.client.methods.{CloseableHttpResponse, HttpGet, HttpPost}
// import org.apache.http.impl.client.{CloseableHttpClient, HttpClients}
// import org.apache.http.message.BasicNameValuePair
// import org.apache.http.util.EntityUtils

import java.nio.charset.Charset
import org.apache.http.config.ConnectionConfig
import org.apache.http.impl.client.{CloseableHttpClient, HttpClientBuilder}
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager
trait ApacheHttpClient extends HttpClient {
  private[this] val config = Config.default

  lazy val httpClient = {
    val builder = HttpClientBuilder.create()

    builder.setConnectionManager {
      val manager = new PoolingHttpClientConnectionManager()
      manager.setMaxTotal(config.getInt("http.max-total-connections"))
      manager.setDefaultMaxPerRoute(config.getInt("http.max-connections-per-route"))
      manager
    }

    builder.setDefaultConnectionConfig {
      val config = ConnectionConfig.custom()
      val charset = Charset.forName("UTF-8")
      config.setCharset(charset)
      config.build()
    }

    new Client(builder.build())
  }

  protected class Client(underlying: CloseableHttpClient) extends Http {
    def GET: HttpRequestBuilder = new ApacheHttpClientRequestBuilder()
  }

  // todo
  // setSslcontext
  // setSSLSocketFactory
  // setDefaultSocketConfig(SocketConfig)
  // setDefaultConnectionConfig(ConnectionConfig)
  // setConnectionTimeToLive(connTimeToLive, connTimeToLiveTimeUnit)
  // setConnectionReuseStrategy(ConnectionReuseStrategy)
  // setKeepAliveStrategy(ConnectionKeepAliveStrategy)
  // setTargetAuthenticationStrategy(AuthenticationStrategy)
  // setProxyAuthenticationStrategy(AuthenticationStrategy)
  // setUserAgent(string)
  // setRetryHandler(HttpRequestRetryHandler)
  // new PoolingHttpClientConnectionManager()
}
