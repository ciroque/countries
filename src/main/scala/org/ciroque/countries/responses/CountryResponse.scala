package org.ciroque.countries.responses

import org.ciroque.countries.model.Country
import spray.json._

object CountryResponseProtocol extends DefaultJsonProtocol with RootJsonFormat[CountryResponse] {
  implicit val CountryResponseFormat = jsonFormat2(CountryResponse.apply)

  override def read(json: JsValue): CountryResponse = CountryResponse(None, None)

  override def write(obj: CountryResponse): JsValue = obj.toJson
}

case class CountryResponse(countries: Option[List[Country]], _links: Option[Map[String, Href]])
