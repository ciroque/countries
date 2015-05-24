package org.ciroque.countries

import com.gettyimages.spray.swagger.SwaggerHttpService
import com.wordnik.swagger.annotations.{ApiOperation, Api}
import com.wordnik.swagger.model.ApiInfo
import org.ciroque.countries.responses.RootResponse
import spray.http.MediaTypes._
import spray.httpx.SprayJsonSupport.sprayJsonMarshaller
import spray.routing.HttpService

import scala.reflect.runtime.universe._

@Api(value = "/countries", description = "Countries micro-service. Exposes basic information about countries.")
trait CountryService extends HttpService {

  val arf = actorRefFactory

  val swaggerService = new SwaggerHttpService {
    override def apiTypes = Seq(typeOf[CountryService])

    override def apiVersion = "1.0"

    override def baseUrl = "/"

    override def docsPath = "documentation"

    override def actorRefFactory = arf

    override def apiInfo = Some(new ApiInfo("Countries", "Countries of the world.", "", "Steve Wagner (scalawagz@outlook.com)", "", ""))
  }

  @ApiOperation(value = "I don't know what this is.", notes = "These are the notes")
  def rootRoute = path("") {
    get {
      respondWithMediaType(`application/json`) {
        complete {
          import org.ciroque.countries.responses.RootResponseProtocol.RootResponseFormat
          RootResponse("Please use the provided search and retrieval endpoints. Documentation can be found at the following URI: /documentation")
        }
      }
    }
  }

  def swaggerUiRoute = get {
    pathPrefix("/documentation") {
      pathEndOrSingleSlash {
        getFromResource("swagger-ui/index.html")
      }
    } ~
      getFromResourceDirectory("swagger-ui")
  }

  def routes = rootRoute ~ swaggerUiRoute
}
