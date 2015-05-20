package org.ciroque.countries

import akka.actor.Actor
import org.ciroque.countries.model.Country
import org.ciroque.countries.queries.{CountryCodeQuery, EmptyQuery}
import org.ciroque.countries.responses.CountryResponse

class CountryQueryActor(countries: List[Country]) extends Actor {
  override def receive: Receive = {
    case None => sender ! CountryResponse(None)
    case EmptyQuery => sender ! CountryResponse(Some(countries))
    case CountryCodeQuery(countryCode) => sender ! CountryResponse(Some(countries.filter(country => country.code.toLowerCase.equals(countryCode.toLowerCase))))
  }
}
