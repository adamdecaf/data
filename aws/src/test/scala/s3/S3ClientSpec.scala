package data.aws
import java.io.File
import data.common.UUID
import org.apache.commons.io.IOUtils
import org.specs2.specification.Scope
import org.specs2.mutable.Specification

object S3ClientSpec extends Specification {

  "tell us if a bucket exists or not" in new context {
    client.bucketExists(bucket) must beTrue
    client.bucketExists(UUID() + UUID()) must beFalse
  }

  "put, read, and delete an item" in new context {
    val filename = UUID()
    client.putItem(bucket, filename, file) must beTrue

    client.getItemContentAsStream(bucket, filename).map { is =>
      IOUtils.toString(is, "utf-8").stripLineEnd
    } must beSome("there is something here")

    client.deleteItems(bucket, filename)
    client.getItemContentAsStream(bucket, filename) must beNone
  }

  trait context extends Scope {
    val client = S3Client.create
    val bucket = "data-aws-testing"
    val file = new File("aws/src/test/resources/test-file")
  }
}
