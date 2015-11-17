package org.ciroque.countries

import java.util.concurrent.TimeUnit

import akka.actor.{ActorRef, Props}
import akka.util.Timeout
import com.wordnik.swagger.annotations._
import org.ciroque.countries.core.MethodTiming
import org.ciroque.countries.model.Country
import org.ciroque.countries.queries._
import org.ciroque.countries.responses.{CountryResponse, Href, RootResponse}
import spray.http.HttpHeaders.RawHeader
import spray.http.MediaTypes._
import spray.http.{HttpResponse, StatusCode, Uri}
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

  final val DO_NOT_INCLUDE_BODY: Boolean = false

  val corsHeaders = List(
    RawHeader("Access-Control-Allow-Origin", "*"),
    RawHeader("Access-Control-Allow-Headers", "Content-Type"),
    RawHeader("Access-Control-Allow-Methods", "GET,HEAD,OPTIONS")
  )

  def performQueryAndRespond(query: Query, uri: Uri, includeBody: Boolean = true): routing.Route = MethodTiming("performQueryAndRespond") {
    val something = templeDataQuery ? query
    val somethingElse = something.mapTo[Option[List[Country]]]

    def statusCode(countries: Option[List[Country]]): (StatusCode, Option[Map[String, Href]]) = countries match {
      case None => (StatusCode.int2StatusCode(404), None)
      case Some(_) => (StatusCode.int2StatusCode(200), Some(buildLinks()))
    }

    def buildLinks(): Map[String, Href] = {
      Map("self" -> new Href(uri.toString(), false))
    }

    respondWithMediaType(`application/json`) {
      respondWithHeaders(corsHeaders) {
        complete {
          somethingElse.map {
            list =>
              import org.ciroque.countries.responses.CountryResponseProtocol._
              import spray.json._
              val (httpStatus, links) = statusCode(list)
              val body = if(includeBody) CountryResponse(list, links).toJson.toString() else ""
              HttpResponse(httpStatus, body)
          }
        }
      }
    }
  }

  @ApiOperation(value = "Hypermedia As The Engine Of Application State starting point.", notes = "", httpMethod = "GET", response = classOf[RootResponse])
  def rootRoute = pathEndOrSingleSlash {
      get {
        respondWithMediaType(`application/json`) {
          complete {
            import org.ciroque.countries.responses.RootResponseProtocol.RootResponseFormat
            RootResponse("Countries of the World", Map("countryCodeSearch" -> "/countries/{countryCode}"))
          }
        }
      } ~ options { respondWithHeaders(corsHeaders) { complete("") } }
    }

  @ApiOperation(value = "All countries end-point", notes = "", httpMethod = "GET"/*, response = classOf[CountryResponse]*/)
  def allCountriesRoute = pathPrefix(Stringz.Routes.Countries) {
    pathEndOrSingleSlash {
      requestUri { uri =>
        get {
          performQueryAndRespond(new EmptyQuery(), uri)
        } ~
        head {
          performQueryAndRespond(new EmptyQuery(), uri, DO_NOT_INCLUDE_BODY)
        } ~ options { respondWithHeaders(corsHeaders) { complete("") } }
      }
    }
  }

  @ApiOperation(value = "Country code search end-point", notes = "Searches for countries using multiple country codes.", httpMethod = "GET")
  @ApiImplicitParams(Array(new ApiImplicitParam(name = "countryCodes", value = "The comma-separated list of country codes to search.", required = true, paramType = "String")))
  def countryCodesQueryRoute = pathPrefix(Stringz.Routes.Countries) {
    pathEndOrSingleSlash {
      requestUri { uri =>
        get {
          parameters("countryCodes") { query =>
            performQueryAndRespond(new CountryCodesQuery(query.split(",").toList), uri)
          }
        } ~
          head {
            parameters("countryCodes") { query =>
              performQueryAndRespond(new CountryCodesQuery(query.split(",").toList), uri, DO_NOT_INCLUDE_BODY)
            }
          } ~ options { respondWithHeaders(corsHeaders) { complete("") } }
      }
    }
  }

  @ApiOperation(value = "Country code search end-point", notes = "Searched for a single country by country code.", httpMethod = "GET"/*, response = classOf[CountryResponse]*/)
  @ApiParam(name = "countryCode", value = "US")
  def countryCodeQueryRoute = pathPrefix(Stringz.Routes.Countries) {
    pathEndOrSingleSlash {
      requestUri { uri =>
        get {
          parameters("countryCode") { query =>
            performQueryAndRespond(new CountryCodeQuery(query), uri)
          }
        } ~
          head {
            parameters("countryCode") { query =>
              performQueryAndRespond(new CountryCodeQuery(query), uri, DO_NOT_INCLUDE_BODY)
            }
          } ~ options { respondWithHeaders(corsHeaders) { complete("") } }
      }
    }
  }

  @ApiOperation(value = "Country name search end-point", notes = "", httpMethod = "GET"/*, response = classOf[CountryResponse]*/)
  @ApiParam(name = "name", value = "Brazil")
  def countryNameQueryRoute = pathPrefix(Stringz.Routes.Countries) {
    pathEndOrSingleSlash {
      requestUri { uri =>
        get {
          parameters("name") { query =>
            performQueryAndRespond(new CountryNameQuery(query), uri)
          }
        } ~
          head {
            parameters("name") { query =>
              performQueryAndRespond(new CountryNameQuery(query), uri, DO_NOT_INCLUDE_BODY)
            }
          } ~ options { respondWithHeaders(corsHeaders) { complete("") } }
      }
    }
  }

  def routes = rootRoute ~ countryCodesQueryRoute ~ countryCodeQueryRoute ~ countryNameQueryRoute ~ allCountriesRoute
}
