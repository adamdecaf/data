package data.http

case class HttpResponse(
  dataFilepath: String,
  contentLength: Long,
  statusCode: Int,
  headers: Map[String, String]
)
