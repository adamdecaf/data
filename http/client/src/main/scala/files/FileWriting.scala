package data.http
import data.common.Logging
import java.io.{File, InputStream, IOException}
import java.nio.file.{Files, Paths}

object FileWriting extends Logging {

  final def writeInputStreamToFile(is: InputStream, path: String): File = {
    lazy val file = new File(path)

    try {
      if (!file.exists) {
        log.warn(s"Path ${path} does not exist, creating it.")
        makeParentDirectories(file.getParentFile)
      }

      val p = Paths.get(path)
      Files.copy(is, p)
      file

    } catch {
      case err: IOException =>
        log.error(s"Caught an exception during file write to '${path}'", err)
        file
    }
  }

  private[this] def makeParentDirectories(parent: File): Unit = try {
    parent.mkdirs()
  } catch {
    case err: IOException =>
      log.error(s"Error while creating parent directories for ${parent.getAbsolutePath}")
  }
}
