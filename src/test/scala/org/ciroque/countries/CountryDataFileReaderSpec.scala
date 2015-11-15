package org.ciroque.countries

import org.ciroque.countries.model.Country
import org.scalatest.{FunSpec, FlatSpec, Matchers}

class CountryDataFileReaderSpec extends FunSpec with Matchers {
  describe("CountryDataFileReader") {

  }
  it("should load a valid file and convert it to a List[Country]") {
    val reader = new CountryDataFileReader("/countries.json")
    val countriesOption = reader.load()
    countriesOption shouldBe 'defined
  }

  it("should return an empty list for an empty data file") {
    val reader = new CountryDataFileReader("/testdata/empty-countries.json")
    val countriesOption = reader.load()
    countriesOption shouldBe Some(List[Country]())

    val countries = countriesOption.get
    countries shouldBe a[List[_]]
    countries.length should be(0)
  }

  it("should return None when given a malformed file") {
    val reader = new CountryDataFileReader("/testdata/malformed-countries.json")
    val countriesOption = reader.load()
    countriesOption should be(None)
  }
}
