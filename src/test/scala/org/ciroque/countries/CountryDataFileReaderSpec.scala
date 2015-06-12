package org.ciroque.countries

import org.ciroque.countries.model.Country
import org.scalatest.{FlatSpec, Matchers}

class CountryDataFileReaderSpec extends FlatSpec with Matchers {
  "CountryDataFileReader" should "load a valid file and convert it to a List[Country]" in {
    val reader = new CountryDataFileReader("/countries.json")
    val countriesOption = reader.load()
    countriesOption shouldBe 'defined
  }

  it should "return an empty list for an empty data file" in {
    val reader = new CountryDataFileReader("/testdata/empty-countries.json")
    val countriesOption = reader.load()
    countriesOption shouldBe Some(List[Country]())

    val countries = countriesOption.get
    countries shouldBe a[List[_]]
    countries.length should be(0)
  }

  it should "return None when given a malformed file" in {
    val reader = new CountryDataFileReader("/testdata/malformed-countries.json")
    val countriesOption = reader.load()
    countriesOption should be(None)
  }
}
