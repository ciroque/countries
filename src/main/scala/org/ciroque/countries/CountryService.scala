package org.ciroque.countries

import java.util.concurrent.TimeUnit

import akka.actor.{ActorRef, Props}
import akka.util.Timeout
import com.wordnik.swagger.annotations.{Api, ApiOperation}
import org.ciroque.countries.model.Country
import org.ciroque.countries.queries.{CountryCodeQuery, CountryNameQuery, EmptyQuery, Query}
import org.ciroque.countries.responses.{CountryResponse, RootResponse}
import spray.http.HttpHeaders.RawHeader
import spray.http.MediaTypes._
import spray.http.{HttpResponse, StatusCode}
import spray.httpx.SprayJsonSupport.sprayJsonMarshaller
import spray.routing
import spray.routing.HttpService

import scala.concurrent.ExecutionContext.Implicits.global

@Api(value = "/countries", description = "Countries micro-service. Exposes basic information about countries.")
trait CountryService extends HttpService {

  import akka.pattern.ask

  implicit val timeout: Timeout = Timeout(3, TimeUnit.SECONDS)
  implicit val countries: Option[List[Country]]
  val templeDataQuery: ActorRef = actorRefFactory.actorOf(Props(classOf[CountryQueryActor], countries.getOrElse(List())), "country-query")

  val corsHeaders = List(
    RawHeader("Access-Control-Allow-Origin", "*"),
    RawHeader("Access-Control-Allow-Headers", "Content-Type"),
    RawHeader("Access-Control-Allow-Methods", "GET")
  )

  def performQueryAndRespond(query: Query): routing.Route = {
    val something = templeDataQuery ? query
    val somethingElse = something.mapTo[Option[List[Country]]]
    def statusCode(countries: Option[List[Country]]): StatusCode = countries match {
      case None => StatusCode.int2StatusCode(404)
      case Some(_) => StatusCode.int2StatusCode(200)
    }
    respondWithMediaType(`application/json`) {
      respondWithHeaders(corsHeaders) {
        complete {
          somethingElse.map {
            list =>
              import spray.json._
              import org.ciroque.countries.responses.CountryResponseProtocol._
              val body = CountryResponse(list).toJson.toString()
              HttpResponse(statusCode(list), body)
          }
        }
      }
    }
  }

  @ApiOperation(value = "Hypermedia As The Engine Of Application State starting point.", notes = "")
  def rootRoute =
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

  @ApiOperation(value = "All countries end-point", notes = "")
  def allCountriesRoute = pathPrefix(Stringz.Routes.Countries) {
    pathEndOrSingleSlash {
      requestUri { uri =>
        get {
          performQueryAndRespond(new EmptyQuery())
        }
      }
    }
  }

  @ApiOperation(value = "Country code search end-point", notes = "")
  def countryCodeQueryRoute = pathPrefix(Stringz.Routes.Countries) {
    pathEndOrSingleSlash {
      requestUri { uri =>
        get {
          parameters("countryCode") { query =>
            performQueryAndRespond(new CountryCodeQuery(query))
          }
        }
      }
    }
  }

  @ApiOperation(value = "Country name search end-point", notes = "")
  def countryNameQueryRoute = pathPrefix(Stringz.Routes.Countries) {
    pathEndOrSingleSlash {
      requestUri { uri =>
        get {
          parameters("name") { query =>
            performQueryAndRespond(new CountryNameQuery(query))
          }
        }
      }
    }
  }

  def routes = rootRoute ~ countryCodeQueryRoute ~ countryNameQueryRoute ~ allCountriesRoute
}
