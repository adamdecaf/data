package data.http
import org.specs2.specification.Scope
import org.specs2.mutable.Specification
import scala.concurrent.Await
import scala.concurrent.duration.Duration
import java.net.UnknownHostException

object HttpSpec extends Specification {

  "capture google.com" in new context {
    val responseF = HttpRequestBuilder.GET("http://google.com").execute()
    val response = Await.result(responseF, Duration("10 seconds"))

    val path = response.dataFilepath
    println(s"Location: ${path}")

    val content = scala.io.Source.fromFile(path).getLines.mkString("\n")
    content  must not(beEmpty)
  }

  "blow up on some non existent website" in new context {
    val responseF = HttpRequestBuilder.GET("http://sdsadksajdaslkdjasdlsjdlsdkjdlkj.com").execute()
    responseF.failed must haveClass[UnknownHostException].await
  }

  trait context extends Scope
}
