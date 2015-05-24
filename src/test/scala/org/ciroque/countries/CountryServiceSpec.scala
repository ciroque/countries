package org.ciroque.countries

import akka.actor.ActorRefFactory
import org.specs2.mutable.Specification
import spray.http.MediaTypes._
import spray.testkit.Specs2RouteTest

class CountryServiceSpec
  extends Specification
  with Specs2RouteTest
  with CountryService {

  lazy val actorRefFactory: ActorRefFactory = system

  "CountryService" should {
    "Return documentation instructions on the root path" in {
      Get() ~> routes ~> check {
        contentType.mediaType mustEqual `application/json`
        responseAs[String] must contain("/documentation")
      }
    }

    "Do something really fun when accessing the swagger docs" in {
      Get("/documentation") ~> routes ~> check {
        contentType.mediaType mustEqual `application/json`
        responseAs[String] must contain("FOO")
      }
    }
  }
}