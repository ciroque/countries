package org.ciroque.countries.responses

import spray.json.DefaultJsonProtocol

object RootResponseProtocol extends DefaultJsonProtocol {
  implicit val RootResponseFormat = jsonFormat1(RootResponse)
}

case class RootResponse(message: String)

//object RootResponse {
//  def RootResponse(msg: String): Unit = {
//    new RootResponse(msg)
//  }
//}
