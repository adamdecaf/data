package data.http
import data.config.Config

// import org.apache.http.HttpEntity
// import org.apache.http.NameValuePair
// import org.apache.http.client.entity.UrlEncodedFormEntity
// import org.apache.http.client.methods.{CloseableHttpResponse, HttpGet, HttpPost}

// import org.apache.http.message.BasicNameValuePair
// import org.apache.http.util.EntityUtils

import java.nio.charset.Charset
import org.apache.http.config.ConnectionConfig
import org.apache.http.impl.client.HttpClientBuilder
import org.apache.http.impl.client.CloseableHttpClient
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager

object ApacheHttpClient {
  private[this] val config = Config.default

  lazy val httpClient: CloseableHttpClient = {
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

    builder.setUserAgent(config.getString("http.user-agent"))

    // import org.apache.http.protocol.BasicHttpContext
    // private[this] val localContext: HttpContext = new BasicHttpContext

    // catch SSLPeerUnverifiedException
    // val resp = try { httpclient.execute(request, localContext) } catch { case e: Throwable => retryFailedHandshake(request, sslRetryAmount) }

    // actually, i think it's better to buffer these bytes to disk and pass along that reference.
    // then we don't store all these bytes in memory

    // val bytes = readToBytesAndClose(resp.getEntity.getContent)
    // val respContent = new ByteArrayInputStream(bytes)
    // val allHeaders = resp.getAllHeaders.toList
    //   (header.getName -> Seq(header.getValue)

    // private[this] val cookieStore = new BasicCookieStore()
    // localContext.setAttribute(ClientContext.COOKIE_STORE, cookieStore)

    // proxy foreach { case FetchProxy(host, port) =>
    //   val socksaddr = new InetSocketAddress(host, port);
    //   localContext.setAttribute("socks.address", socksaddr)
    // }

    // val requestConfig = RequestConfig
    //   .custom
    //   .setConnectionRequestTimeout(httpConnectionRequestTimeout) // time waiting for a connection from the pool
    //   .setConnectTimeout(httpConnectTimeout) // time waiting to connect to a host
    //   .setSocketTimeout(httpSocketTimeout) // time waiting for data to be received from a host
    //   .setExpectContinueEnabled(false)
    //   .setCircularRedirectsAllowed(true)
    //   .build

    // val socketConfig = SocketConfig
    //   .custom
    //   .setSoTimeout(httpSocketTimeout)
    //   .setTcpNoDelay(true)
    //   .build

    // val c = HttpClients.custom
    //   .setConnectionManager(connectionManager)
    //   .setRequestExecutor(new InstrumentedHttpRequestExecutor(metricRegistry, HostMetricNameStrategy))
    //   .setDefaultSocketConfig(socketConfig)
    //   .setDefaultRequestConfig(requestConfig)

    // val dc = new DecompressingHttpClient(c)

    // if (httpCachingEnabled)
    //   new CachingHttpClient(dc)
    // else
    //   dc

    // needed?
    // setDefaultSocketConfig(SocketConfig)
    // setConnectionTimeToLive(connTimeToLive, connTimeToLiveTimeUnit)
    // setConnectionReuseStrategy(ConnectionReuseStrategy)
    // setKeepAliveStrategy(ConnectionKeepAliveStrategy)
    // setRetryHandler(HttpRequestRetryHandler)

    // later
    // setSslcontext
    // setSSLSocketFactory

    // later
    // setTargetAuthenticationStrategy(AuthenticationStrategy)
    // setProxyAuthenticationStrategy(AuthenticationStrategy)

    builder.build()
  }
}
