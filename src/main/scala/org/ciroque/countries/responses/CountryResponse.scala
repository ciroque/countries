package org.ciroque.countries.responses

import org.ciroque.countries.model.Country
import spray.json.DefaultJsonProtocol
import org.ciroque.countries.model.CountryProtocol._

object CountryResponseProtocol extends DefaultJsonProtocol {
  implicit val CountryResponseFormat = jsonFormat1(CountryResponse.apply)
}

case class CountryResponse(countries: Option[List[Country]])

object CountryResponse {
  def CountryResponse(countries: Option[List[Country]]): Unit = {
    new CountryResponse(countries)
  }
}
