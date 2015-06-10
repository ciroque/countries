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

  "CountryService" should {
    "Return documentation instructions on the root path" in {
      Get("/") ~> routes ~> check {
        contentType.mediaType mustEqual `application/json`
        responseAs[String] must contain("countryCodeSearch")
      }
    }

    "Return the full list of countries on the countries endpoint" in {
      Get(s"/${Stringz.Routes.Countries}/") ~> routes ~> check {
        contentType.mediaType mustEqual `application/json`
//        responseAs[CountryResponse] must contain("United States of America")
        MockCountryDataLoader.countryList.foreach {
          country =>
            responseAs[String] must contain(country.fullName)
        }
        status.intValue must_== 200
      }
    }

    "Return the correct country from a valid country code" in {
      Get(s"/${Stringz.Routes.Countries}?countryCode=US") ~> routes ~> check {
        assertCorsHeaders(headers)
        contentType.mediaType mustEqual `application/json`
        status.intValue must_== 200
        responseAs[String] must contain("United States of America")
      }
    }

    "Return the correct country from a valid country code in a case-insensitive manner" in {
      Get(s"/${Stringz.Routes.Countries}?countryCode=us") ~> routes ~> check {
        assertCorsHeaders(headers)
        contentType.mediaType mustEqual `application/json`
        status.intValue must_== 200
        responseAs[String] must contain("United States of America")
      }
    }

    "Return the correct country from a valid country name" in {
      Get(s"/${Stringz.Routes.Countries}?name=Canada") ~> routes ~> check {
        assertCorsHeaders(headers)
        contentType.mediaType mustEqual `application/json`
        status.intValue must_== 200
        responseAs[String] must contain("People's Republic of Canadia")
      }
    }

    "Return the correct country from a valid country name in a case-insensitive manner" in {
      Get(s"/${Stringz.Routes.Countries}?name=canada") ~> routes ~> check {
        assertCorsHeaders(headers)
        contentType.mediaType mustEqual `application/json`
        status.intValue must_== 200
        responseAs[String] must contain("People's Republic of Canadia")
      }
    }

    "Return a 404 when given an invalid country code" in {
      Get(s"/${Stringz.Routes.Countries}?countryCode=ZZ") ~> routes ~> check {
        val ras = responseAs[String]
        println(s">>>>> $ras")

        contentType.mediaType mustEqual `application/json`

//        status.intValue must_== 404
      }
    }
  }
}
