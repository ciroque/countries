package org.ciroque.countries.responses

import spray.json._

object ErrorResponseProtocol extends DefaultJsonProtocol with RootJsonFormat[ErrorResponse] {
  implicit val ErrorResponseFormat = jsonFormat1(ErrorResponse.apply)

  override def read(json: JsValue): ErrorResponse = ErrorResponse(null)

  override def write(obj: ErrorResponse): JsValue = obj.toJson
}

case class ErrorResponse(message: String)

object ErrorResponse {
  def ErrorResponse(message: String) = new ErrorResponse(message)
}
