package org.ciroque.countries.responses

import akka.actor.Actor
import org.ciroque.countries.{CountryDataLoader, CountryService}

class CountryServiceActor(loader: CountryDataLoader)
  extends Actor
  with CountryService {
  override def receive: Receive = runRoute(routes)
  def actorRefFactory = context
}
