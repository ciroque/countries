package org.ciroque.countries

import org.ciroque.countries.model.Country

trait CountryDataLoader {
  def load(): Option[List[Country]]
}
