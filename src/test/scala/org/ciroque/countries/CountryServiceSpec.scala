package org.ciroque.countries

import akka.actor.ActorRefFactory
import org.ciroque.countries.model.Country
import org.specs2.mutable.Specification
import spray.http.HttpHeader
import spray.http.HttpHeaders.RawHeader
import spray.http.MediaTypes._
import spray.testkit.Specs2RouteTest

class CountryServiceSpec
  extends Specification
  with Specs2RouteTest
  with CountryService {

  override lazy implicit val countries: Option[List[Country]] = MockCountryDataLoader.load()

  lazy val actorRefFactory: ActorRefFactory = system

  def assertCorsHeaders(headers: List[HttpHeader]) = {
    headers.contains(RawHeader("Access-Control-Allow-Headers", "Content-Type"))
    headers.contains(RawHeader("Access-Control-Allow-Methods", "GET"))
    headers.contains(RawHeader("Access-Control-Allow-Origin", "example.com"))
  }

  def assertHalLinks(body: String, supplemental: String) = {
    body must contain("_links")
    body must contain("self")
    body must contain(supplemental)
  }

  def assertNoHalLinks(body: String) = {
    body must not contain "_links"
    body must not contain "self"
  }

  "CountryService" should {
    "Return documentation instructions on the root path" in {
      Get("/") ~> routes ~> check {
        status.intValue must_== 200
        assertCorsHeaders(headers)
        contentType.mediaType mustEqual `application/json`
        responseAs[String] must contain("countryCodeSearch")
      }
    }

    "Return the full list of countries on the countries endpoint" in {
      Get(s"/${Stringz.Routes.Countries}/") ~> routes ~> check {
        status.intValue must_== 200
        assertCorsHeaders(headers)
        assertHalLinks(responseAs[String], "")
        MockCountryDataLoader.countryList.foreach {
          country =>
            responseAs[String] must contain(country.fullName)
        }
        contentType.mediaType mustEqual `application/json`
      }
    }

    "Return the correct country from a valid country code" in {
      val query = "?countryCode=US"
      Get(s"/${Stringz.Routes.Countries}$query") ~> routes ~> check {
        status.intValue must_== 200
        assertCorsHeaders(headers)
        assertHalLinks(responseAs[String], query)
        contentType.mediaType mustEqual `application/json`
        responseAs[String] must contain("United States of America")
      }
    }

    "Return the correct country from a valid country code in a case-insensitive manner" in {
      val query = "?countryCode=us"
      Get(s"/${Stringz.Routes.Countries}$query") ~> routes ~> check {
        status.intValue must_== 200
        assertCorsHeaders(headers)
        assertHalLinks(responseAs[String], query)
        contentType.mediaType mustEqual `application/json`
        responseAs[String] must contain("United States of America")
      }
    }

    "Return the correct country from a valid country name" in {
      val query = "?name=Canada"
      Get(s"/${Stringz.Routes.Countries}$query") ~> routes ~> check {
        status.intValue must_== 200
        assertCorsHeaders(headers)
        assertHalLinks(responseAs[String], query)
        contentType.mediaType mustEqual `application/json`
        responseAs[String] must contain("People's Republic of Canadia")
      }
    }

    "Return the correct country from a valid country name in a case-insensitive manner" in {
      val query = "?name=canada"
      Get(s"/${Stringz.Routes.Countries}$query") ~> routes ~> check {
        status.intValue must_== 200
        assertCorsHeaders(headers)
        assertHalLinks(responseAs[String], query)
        contentType.mediaType mustEqual `application/json`
        responseAs[String] must contain("People's Republic of Canadia")
      }
    }

    "Return a 404 when given an invalid country code" in {
      val query = "?countryCode=ZZ"
      Get(s"/${Stringz.Routes.Countries}$query") ~> routes ~> check {
        status.intValue must_== 404
        assertCorsHeaders(headers)
        assertNoHalLinks(responseAs[String])
        contentType.mediaType mustEqual `application/json`
        responseAs[String] mustEqual "{}"
      }
    }

    "Return a 404 when given an invalid country name" in {
      val query = "?name=bolshevic"
      Get(s"/${Stringz.Routes.Countries}$query") ~> routes ~> check {
        status.intValue must_== 404
        assertCorsHeaders(headers)
        assertNoHalLinks(responseAs[String])
        contentType.mediaType mustEqual `application/json`
        responseAs[String] mustEqual "{}"
      }
    }

    "Return a 404 when given an invalid country name" in {
      val query = "?name=bolshevic"
      Get(s"/${Stringz.Routes.Countries}$query") ~> routes ~> check {
        status.intValue must_== 404
        assertCorsHeaders(headers)
        assertNoHalLinks(responseAs[String])
        contentType.mediaType mustEqual `application/json`
        responseAs[String] mustEqual "{}"
      }
    }

    "Return results of a multiple country code query" in {
      val query = "?countryCodes=US,CA,LU"
      Get(s"/${Stringz.Routes.Countries}$query") ~> routes ~> check {
        status.intValue must_== 200
        assertCorsHeaders(headers)
        assertHalLinks(responseAs[String], query)
        contentType.mediaType mustEqual `application/json`
        responseAs[String] must contain("United States of America")
        responseAs[String] must contain("Canada")
        responseAs[String] must contain("Luxembourg")
        responseAs[String] must not contain "Mexico"
      }
    }
  }
}
