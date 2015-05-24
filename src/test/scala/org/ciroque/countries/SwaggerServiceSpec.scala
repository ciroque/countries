package org.ciroque.countries

import akka.actor.ActorRefFactory
import org.specs2.mutable.Specification
import spray.testkit.Specs2RouteTest

class SwaggerServiceSpec
  extends Specification
  with Specs2RouteTest
  with SwaggerService {

  lazy val actorRefFactory: ActorRefFactory = system

  "SwaggerService" should {
//    "Supply API docs via swagger" in {
//      Get("/documentation") ~> routes ~> check {
//        contentType.mediaType mustEqual `text/html`
//        responseAs[String] must contain("Countries of the world.")
//      }
//    }
  }
}
