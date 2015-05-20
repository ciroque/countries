package org.ciroque.countries.model

case class Country(code: String, lon: Double, lat: Double, name: String, fullName: String)

object Country {
  def Empty: Country = new Country(null, 0.0, 0.0, null, null)
}

object CountryProtocol extends spray.json.DefaultJsonProtocol {
  implicit val CountryInfoProtocol = jsonFormat5(Country.apply)
}
