package org.ciroque.countries

import akka.actor.{ActorSystem, Props}
import akka.io.IO
import akka.pattern.ask
import akka.util.Timeout
import spray.can.Http

import scala.concurrent.duration._

object ApplicationMain extends App {
  implicit val system = ActorSystem("countries-actor-system")
  implicit val timeout = Timeout(5.seconds)

  val pingActor = system.actorOf(PingActor.props, "ping-actor")

  val dataFileReader = new CountryDataFileReader("/countries.json")
  val service = system.actorOf(Props(new CountryServiceActor(dataFileReader)), "country-service-actor")
  IO(Http) ? Http.Bind(service, interface = "0.0.0.0", port = 35784)
}
