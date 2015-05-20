package org.ciroque.countries

import akka.actor.ActorSystem

object ApplicationMain extends App {
  val system = ActorSystem("MyActorSystem")
  val pingActor = system.actorOf(PingActor.props, "pingActor")
  system.awaitTermination()
//  IO(Http) ? Http.Bind(service, interface = "localhost", port = 35762)
}
