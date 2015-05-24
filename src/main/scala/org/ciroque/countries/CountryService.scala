package org.ciroque.countries

import com.wordnik.swagger.annotations.{Api, ApiOperation}
import org.ciroque.countries.responses.RootResponse
import spray.http.MediaTypes._
import spray.httpx.SprayJsonSupport.sprayJsonMarshaller
import spray.routing.HttpService

@Api(value = "/countries", description = "Countries micro-service. Exposes basic information about countries.")
trait CountryService extends HttpService {

  @ApiOperation(value = "I don't know what this is.", notes = "These are the notes")
  def rootRoute = path("countries") {
    get {
      respondWithMediaType(`application/json`) {
        complete {
          import org.ciroque.countries.responses.RootResponseProtocol.RootResponseFormat
          RootResponse("Please use the provided search and retrieval endpoints. Documentation can be found at the following URI: /documentation")
        }
      }
    }
  }

  def routes = rootRoute
}
