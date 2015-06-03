package org.ciroque.countries

import akka.actor.ActorRefFactory
import org.ciroque.countries.model.Country
import org.specs2.mutable.Specification
import spray.http.MediaTypes._
import spray.testkit.Specs2RouteTest

class CountryServiceSpec
  extends Specification
  with Specs2RouteTest
  with CountryService {

  override lazy implicit val countries: Option[List[Country]] = MockCountryDataLoader.load()

  lazy val actorRefFactory: ActorRefFactory = system

  "CountryService" should {
    "Return documentation instructions on the root path" in {
      Get("/countries") ~> routes ~> check {
        contentType.mediaType mustEqual `application/json`
        responseAs[String] must contain("countryCodeSearch")
      }
    }

    "Return the correct country from a valid country code" in {
      Get(s"/${Stringz.Routes.Countries}?countryCode=US") ~> routes ~> check {
        contentType.mediaType mustEqual `application/json`
        responseAs[String] must contain("United States of America")
      }
    }
  }
}
