package org.ciroque.countries.model

case class Country(lon: Double, lat: Double, name: String, fullName: String)

object Country {
  def Empty: Country = new Country(0.0, 0.0, null, null)
}

object CountryProtocol extends spray.json.DefaultJsonProtocol {
  implicit val CountryInfoProtocol = jsonFormat4(Country.apply)
}
