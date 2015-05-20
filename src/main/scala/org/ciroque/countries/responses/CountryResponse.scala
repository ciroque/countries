package org.ciroque.countries.responses

import org.ciroque.countries.model.Country

case class CountryResponse(countries: Option[List[Country]])

object CountryResponse {
  def CountryResponse(countries: Option[List[Country]]): Unit = {
    new CountryResponse(countries)
  }
}
