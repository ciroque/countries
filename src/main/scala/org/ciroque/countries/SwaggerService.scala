package org.ciroque.countries

import spray.routing.HttpService

trait SwaggerService extends HttpService {

  def routes = get {
    pathPrefix("documentation") {
      pathEndOrSingleSlash {
        getFromResource("swagger-ui/index.html")
      }
    } ~
      getFromResourceDirectory("swagger-ui")
  }
}
