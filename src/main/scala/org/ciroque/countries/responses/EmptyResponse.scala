package org.ciroque.countries.responses

import spray.json.{JsObject, JsValue, RootJsonFormat, DefaultJsonProtocol}

object EmptyResponse extends DefaultJsonProtocol with RootJsonFormat[EmptyResponse] {
  override def read(json: JsValue): EmptyResponse = EmptyResponse()
  override def write(obj: EmptyResponse): JsValue = JsObject()
}

case class EmptyResponse()

