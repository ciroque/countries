package org.ciroque.countries

import com.wordnik.swagger.annotations.{Api, ApiOperation}
import org.ciroque.countries.responses.RootResponse
import spray.http.MediaTypes._
import spray.httpx.SprayJsonSupport.sprayJsonMarshaller
import spray.routing.HttpService

@Api(value = "/countries", description = "Countries micro-service. Exposes basic information about countries.")
trait CountryService extends HttpService {

  @ApiOperation(value = "Hypermedia As The Engine Of Application State starting point.", notes = "These are the notes")
  def rootRoute = pathPrefix("countries") {
    pathEndOrSingleSlash {
      get {
        respondWithMediaType(`application/json`) {
          complete {
            import org.ciroque.countries.responses.RootResponseProtocol.RootResponseFormat
            RootResponse("Countries of the World", Map("countryCodeSearch" -> "/countries/{countryCode}"))
          }
        }
      }
    }
  }

  def routes = rootRoute
}
