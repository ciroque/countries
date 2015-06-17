package org.ciroque.countries.responses

import spray.json._

case class Href(href: String, templated: Boolean = false)

object Href extends DefaultJsonProtocol with RootJsonFormat[Href] {
  implicit val HrefFormat = jsonFormat2(Href.apply)

  override def write(obj: Href): JsValue = obj.toJson

  override def read(json: JsValue): Href = new Href(null, false)
}
