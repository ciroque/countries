package org.ciroque.countries

import org.ciroque.countries.model.Country

object MockCountryDataLoader extends CountryDataLoader {
  override def load(): Option[List[Country]] = {
    Some(countryList)
  }

  val countryList = List(
    Country("US", 0.0, 0.0, "USA", "United States of America"),
    Country("MX", 0.0, 0.0, "Mexico", "Mexico"),
    Country("CA", 0.0, 0.0, "Canada", "People's Republic of Canadia"),
    Country("LU", 0.0, 0.0, "Luxembourg", "Grand Duchy of Luxembourg")
  )
}
