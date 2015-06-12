package org.ciroque.countries

import org.ciroque.countries.model.Country

object MockCountryDataLoader extends CountryDataLoader {
  override def load(): Option[List[Country]] = {
    Some(countryList)
  }

  val countryList = List(
    Country("US", "USA", "United States of America"),
    Country("MX", "Mexico", "Mexico"),
    Country("CA", "Canada", "People's Republic of Canadia"),
    Country("LU", "Luxembourg", "Grand Duchy of Luxembourg")
  )
}
