package org.ciroque.countries.model

case class Country(code: String, name: String, fullName: String)

object Country extends spray.json.DefaultJsonProtocol {
  def Empty: Country = new Country(null, null, null)

  implicit val CountryInfoProtocol = jsonFormat3(Country.apply)
}
