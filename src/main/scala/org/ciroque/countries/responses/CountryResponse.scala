package org.ciroque.countries.responses

import org.ciroque.countries.model.Country
import org.ciroque.countries.model.CountryProtocol._
import spray.json._

object CountryResponseProtocol extends DefaultJsonProtocol with RootJsonFormat[CountryResponse] {
  implicit val CountryResponseFormat = jsonFormat1(CountryResponse.apply)

  override def read(json: JsValue): CountryResponse = CountryResponse(None)

  override def write(obj: CountryResponse): JsValue = obj.toJson
}

case class CountryResponse(countries: Option[List[Country]])

object CountryResponse {
  def CountryResponse(countries: Option[List[Country]]): Unit = {
    new CountryResponse(countries)
  }
}
