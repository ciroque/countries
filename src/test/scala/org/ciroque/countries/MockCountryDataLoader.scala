package org.ciroque.countries

import org.ciroque.countries.model.{Country, ParentGeoNode}

object MockCountryDataLoader extends CountryDataLoader {
  override def load(): Option[List[Country]] = {
    Some(countryList)
  }

  val countryList = List(
    Country("US", "USA", "United States of America", ParentGeoNode.Origin),
    Country("MX", "Mexico", "Mexico", ParentGeoNode.Origin),
    Country("CA", "Canada", "People's Republic of Canadia", ParentGeoNode.Origin),
    Country("LU", "Luxembourg", "Grand Duchy of Luxembourg", ParentGeoNode.Origin)
  )
}
