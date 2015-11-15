package org.ciroque.countries

import akka.actor.ActorRefFactory
import org.ciroque.countries.model.Country
import org.ciroque.countries.utils.MockCountryDataLoader
import org.scalatest.{FunSpec, Matchers}
import spray.http.HttpHeaders.RawHeader
import spray.http.MediaTypes._
import spray.http.{HttpHeader, StatusCodes}
import spray.testkit.ScalatestRouteTest

class CountryServiceSpec
  extends FunSpec
  with Matchers
  with ScalatestRouteTest
  with CountryService {

  override lazy implicit val countries: Option[List[Country]] = MockCountryDataLoader.load()

  lazy val actorRefFactory: ActorRefFactory = system

  def assertCorsHeaders(headers: List[HttpHeader]) = {
    headers.contains(RawHeader("Access-Control-Allow-Headers", "Content-Type"))
    headers.contains(RawHeader("Access-Control-Allow-Methods", "GET"))
    headers.contains(RawHeader("Access-Control-Allow-Origin", "example.com"))
    headers.contains(RawHeader("Content-Type", `application/json`.toString()))
  }

  def assertHalLinks(body: String, supplemental: String) = {
    body should include("_links")
    body should include("self")
    body should include(supplemental)
  }

  def assertNoHalLinks(body: String) = {
    body should not contain "_links"
    body should not contain "self"
  }

  describe("CountryService") {

    describe("HTTP GET") {
      it("returns documentation instructions on the root path") {
        Get("/") ~> routes ~> check {
          status should be(StatusCodes.OK)
          assertCorsHeaders(headers)
          contentType.mediaType should be(`application/json`)
          responseAs[String] should include("countryCodeSearch")
        }
      }

      it("returns the full list of countries on the countries endpoint") {
        Get(s"/${Stringz.Routes.Countries}/") ~> routes ~> check {
          status should be(StatusCodes.OK)
          assertCorsHeaders(headers)
          assertHalLinks(responseAs[String], "")
          MockCountryDataLoader.countryList.foreach {
            country =>
              responseAs[String] should include(country.fullName)
          }
          contentType.mediaType should be(`application/json`)
        }
      }

      it("returns the correct country from a valid country code") {
        val query = "?countryCode=US"
        Get(s"/${Stringz.Routes.Countries}$query") ~> routes ~> check {
          status should be(StatusCodes.OK)
          assertCorsHeaders(headers)
          assertHalLinks(responseAs[String], query)
          contentType.mediaType shouldEqual `application/json`
          responseAs[String] should include("United States of America")
        }
      }

      it("returns the correct country from a valid country code in a case-insensitive manner") {
        val query = "?countryCode=us"
        Get(s"/${Stringz.Routes.Countries}$query") ~> routes ~> check {
          status should be(StatusCodes.OK)
          assertCorsHeaders(headers)
          assertHalLinks(responseAs[String], query)
          contentType.mediaType shouldEqual `application/json`
          responseAs[String] should include("United States of America")
        }
      }

      it("returns the correct country from a valid country name") {
        val query = "?name=Canada"
        Get(s"/${Stringz.Routes.Countries}$query") ~> routes ~> check {
          status should be(StatusCodes.OK)
          assertCorsHeaders(headers)
          assertHalLinks(responseAs[String], query)
          contentType.mediaType shouldEqual `application/json`
          responseAs[String] should include("People's Republic of Canadia")
        }
      }

      it("returns the correct country from a valid country name in a case-insensitive manner") {
        val query = "?name=canada"
        Get(s"/${Stringz.Routes.Countries}$query") ~> routes ~> check {
          status should be(StatusCodes.OK)
          assertCorsHeaders(headers)
          assertHalLinks(responseAs[String], query)
          contentType.mediaType shouldEqual `application/json`
          responseAs[String] should include("People's Republic of Canadia")
        }
      }

      it("returns a 404 when given an invalid country code") {
        val query = "?countryCode=ZZ"
        Get(s"/${Stringz.Routes.Countries}$query") ~> routes ~> check {
          status should be(StatusCodes.NotFound)
          assertCorsHeaders(headers)
          assertNoHalLinks(responseAs[String])
          contentType.mediaType shouldEqual `application/json`
          responseAs[String] shouldEqual "{}"
        }
      }

      it("returns a 404 when given an invalid country name") {
        val query = "?name=bolshevic"
        Get(s"/${Stringz.Routes.Countries}$query") ~> routes ~> check {
          status should be(StatusCodes.NotFound)
          assertCorsHeaders(headers)
          assertNoHalLinks(responseAs[String])
          contentType.mediaType shouldEqual `application/json`
          responseAs[String] shouldEqual "{}"
        }
      }

      it("returns results of a multiple country code query") {
        val query = "?countryCodes=US,CA,LU"
        Get(s"/${Stringz.Routes.Countries}$query") ~> routes ~> check {
          status should be(StatusCodes.OK)
          assertCorsHeaders(headers)
          assertHalLinks(responseAs[String], query)
          contentType.mediaType shouldEqual `application/json`
          responseAs[String] should include("United States of America")
          responseAs[String] should include("Canada")
          responseAs[String] should include("Luxembourg")
          responseAs[String] should not contain "Mexico"
        }
      }
    }

    describe("HTTP HEAD") {
      it("returns the full list of countries on the countries endpoint") {
        Head(s"/${Stringz.Routes.Countries}/") ~> routes ~> check {
          status should be(StatusCodes.OK)
          assertCorsHeaders(headers)
          responseAs[String] should be("")
        }
      }

      it("returns the correct country from a valid country code") {
        val query = "?countryCode=US"
        Head(s"/${Stringz.Routes.Countries}$query") ~> routes ~> check {
          status should be(StatusCodes.OK)
          assertCorsHeaders(headers)
          responseAs[String] should be("")
        }
      }

      it("returns the correct country from a valid country code in a case-insensitive manner") {
        val query = "?countryCode=us"
        Head(s"/${Stringz.Routes.Countries}$query") ~> routes ~> check {
          status should be(StatusCodes.OK)
          assertCorsHeaders(headers)
          responseAs[String] should be("")
        }
      }

      it("returns the correct country from a valid country name") {
        val query = "?name=Canada"
        Head(s"/${Stringz.Routes.Countries}$query") ~> routes ~> check {
          status should be(StatusCodes.OK)
          assertCorsHeaders(headers)
          responseAs[String] should be("")
        }
      }

      it("returns the correct country from a valid country name in a case-insensitive manner") {
        val query = "?name=canada"
        Head(s"/${Stringz.Routes.Countries}$query") ~> routes ~> check {
          status should be(StatusCodes.OK)
          assertCorsHeaders(headers)
          responseAs[String] should be("")
        }
      }

      it("returns a 404 when given an invalid country code") {
        val query = "?countryCode=ZZ"
        Head(s"/${Stringz.Routes.Countries}$query") ~> routes ~> check {
          status should be(StatusCodes.NotFound)
          assertCorsHeaders(headers)
          responseAs[String] should be("")
        }
      }

      it("returns a 404 when given an invalid country name") {
        val query = "?name=bolshevic"
        Head(s"/${Stringz.Routes.Countries}$query") ~> routes ~> check {
          status should be(StatusCodes.NotFound)
          assertCorsHeaders(headers)
          responseAs[String] should be("")
        }
      }

      it("returns results of a multiple country code query") {
        val query = "?countryCodes=US,CA,LU"
        Head(s"/${Stringz.Routes.Countries}$query") ~> routes ~> check {
          status should be(StatusCodes.OK)
          assertCorsHeaders(headers)
          responseAs[String] should be("")
        }
      }
    }

    describe("HTTP OPTIONS") {
      it("returns the full list of countries on the countries endpoint") {
        Options(s"/${Stringz.Routes.Countries}/") ~> routes ~> check {
          status should be(StatusCodes.OK)
          assertCorsHeaders(headers)
          responseAs[String] should be("")
        }
      }

      it("returns the correct country from a valid country code") {
        val query = "?countryCode=US"
        Options(s"/${Stringz.Routes.Countries}$query") ~> routes ~> check {
          status should be(StatusCodes.OK)
          assertCorsHeaders(headers)
          responseAs[String] should be("")
        }
      }

      it("returns the correct country from a valid country code in a case-insensitive manner") {
        val query = "?countryCode=us"
        Options(s"/${Stringz.Routes.Countries}$query") ~> routes ~> check {
          status should be(StatusCodes.OK)
          assertCorsHeaders(headers)
          responseAs[String] should be("")
        }
      }

      it("returns the correct country from a valid country name") {
        val query = "?name=Canada"
        Options(s"/${Stringz.Routes.Countries}$query") ~> routes ~> check {
          status should be(StatusCodes.OK)
          assertCorsHeaders(headers)
          responseAs[String] should be("")
        }
      }

      it("returns the correct country from a valid country name in a case-insensitive manner") {
        val query = "?name=canada"
        Options(s"/${Stringz.Routes.Countries}$query") ~> routes ~> check {
          status should be(StatusCodes.OK)
          assertCorsHeaders(headers)
          responseAs[String] should be("")
        }
      }

      it("returns a 200 when given an invalid country code") {
        val query = "?countryCode=ZZ"
        Options(s"/${Stringz.Routes.Countries}$query") ~> routes ~> check {
          status should be(StatusCodes.OK)
          assertCorsHeaders(headers)
          responseAs[String] should be("")
        }
      }

      it("returns a 200 when given an invalid country name") {
        val query = "?name=bolshevic"
        Options(s"/${Stringz.Routes.Countries}$query") ~> routes ~> check {
          status should be(StatusCodes.OK)
          assertCorsHeaders(headers)
          responseAs[String] should be("")
        }
      }

      it("returns results of a multiple country code query") {
        val query = "?countryCodes=US,CA,LU"
        Options(s"/${Stringz.Routes.Countries}$query") ~> routes ~> check {
          status should be(StatusCodes.OK)
          assertCorsHeaders(headers)
          responseAs[String] should be("")
        }
      }
    }
  }
}
