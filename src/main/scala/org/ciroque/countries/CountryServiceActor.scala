package org.ciroque.countries

import akka.actor.ActorRefFactory
import com.gettyimages.spray.swagger.SwaggerHttpService
import com.wordnik.swagger.model.ApiInfo
import spray.routing.HttpServiceActor

import scala.reflect.runtime.universe._

class CountryServiceActor(loader: CountryDataLoader)
  extends HttpServiceActor {

  override def actorRefFactory = context

  val countryService = new CountryService {
    implicit lazy val countries = loader.load()
    override implicit def actorRefFactory: ActorRefFactory =  context
  }

  var swaggerService = new SwaggerService {
    override implicit def actorRefFactory: ActorRefFactory =  context
  }

  val swaggerHttpService = new SwaggerHttpService {
    override def apiTypes = Seq(typeOf[CountryService])
    override def apiVersion = "1.0"
    override def baseUrl = "/"
    override def docsPath = "api-docs"
    override def actorRefFactory = context
    override def apiInfo = Some(new ApiInfo("Countries", "Countries of the world.", "", "Steve Wagner (scalawagz@outlook.com)", "", ""))
  }

  override def receive: Receive = runRoute(countryService.routes ~ swaggerHttpService.routes ~ swaggerService.routes)
}
