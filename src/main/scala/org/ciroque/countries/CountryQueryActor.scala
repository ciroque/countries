package org.ciroque.countries

import akka.actor.Actor
import org.ciroque.countries.model.Country
import org.ciroque.countries.queries.{CountryNameQuery, CountryCodeQuery, EmptyQuery}
import org.ciroque.countries.responses.CountryResponse

class CountryQueryActor(countries: List[Country]) extends Actor {
  override def receive: Receive = {
    case None => sender ! CountryResponse(None)
    case empty: EmptyQuery => println(s"#### CountryQueryActor:: EmptyQuery"); sender ! CountryResponse(Some(countries))
    case CountryCodeQuery(countryCode) =>  sender ! CountryResponse(Some(countries.filter(country => country.code.toLowerCase.equals(countryCode.toLowerCase))))
    case CountryNameQuery(name) => sender ! CountryResponse(Some(countries.filter(country => country.name.toLowerCase.contains(name.toLowerCase))))
    case _ => println(s"!!!! CountryQueryActor:: Something else"); throw new IllegalArgumentException("Not sure what happened...")
  }
}
