package data.http

case class RawHttpResponse(
  dataFilepath: String,
  contentLength: Long,
  statusCode: Int,
  headers: Map[String, String]
)
