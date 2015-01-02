package data.aws
import data.config.Config
import data.common.Logging
import java.io.{ByteArrayInputStream, File, InputStream}
import com.amazonaws.ClientConfiguration
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.services.s3.AmazonS3Client
import com.amazonaws.services.s3.model._
import org.apache.commons.io.IOUtils

object S3Client {
  lazy val create: S3Client = new S3Client(AWSConfig.awsCredentials, AWSConfig.awsConfig)
}

final class S3Client(credentials: BasicAWSCredentials, clientConfig: ClientConfiguration) extends Logging {
  private[this] val config = Config.default
  private[this] val client = new AmazonS3Client(credentials, clientConfig)

  def bucketExists(name: String): Boolean = {
    if (name.isEmpty) {
      false
    } else {
      client.doesBucketExist(name)
    }
  }

  def getItemContentAsStream(bucket: String, key: String): Option[InputStream] = {
    val request = new GetObjectRequest(bucket, key)
    val maxRetries = config.getInt("aws.s3.max-retries")

    def attempt(tries: Int = 0): Option[InputStream] = {
      if (tries >= maxRetries) {
        log.warn("Giving up trying to s3 S3Object under bucket: '${bucket}' and key: '${key}' due to too many retries.")
        None
      } else {
        try {
          val obj = client.getObject(request)
          val bytes = IOUtils.toByteArray(obj.getObjectContent)
          obj.close
          Some(new ByteArrayInputStream(bytes))
        } catch {
          case err: AmazonS3Exception =>
            if (err.getMessage.contains("The specified key does not exist")) {
              log.debug(s"The object under '${key}' in bucket '${bucket}' doesn't exist.")
              None
            } else {
              log.warn("Got an exception from S3 when reading an object scheduling for a retry.", err)
              attempt(tries + 1)
            }
        }
      }
    }

    attempt()
  }

  def putItem(bucket: String, key: String, content: File): Boolean = {
    val req = new PutObjectRequest(bucket, key, content)
    val maxRetries = config.getInt("aws.s3.max-retries")

    def attempt(tries: Int = 0): Boolean = {
      if (tries >= maxRetries) {
        log.warn("Giving up trying to put file from '${content.getAbsolutePath}' to s3 in bucket: '${bucket}' and key: '${key}'.")
        false
      } else {

        try {
          client.putObject(req)
          true
        } catch {
          case err: AmazonS3Exception =>
            log.warn("Got an exception from S3 when putting an object scheduling for a retry.", err)
            attempt(tries + 1)
        }

      }
    }

    attempt()
  }

  def deleteItems(bucket: String, itemNames: String*): Unit = {
    if (bucket.nonEmpty) {
      itemNames.filter(_.nonEmpty).foreach(client.deleteObject(bucket, _))
    }
  }
}
