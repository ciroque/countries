package org.ciroque.countries

import akka.actor.Actor
import org.ciroque.countries.model.Country
import org.ciroque.countries.queries.{CountryCodeQuery, CountryNameQuery, EmptyQuery}

class CountryQueryActor(countries: List[Country]) extends Actor {
  override def receive: Receive = {
    case None => sender ! None
    case empty: EmptyQuery => sender ! Some(countries)
    case CountryCodeQuery(countryCode) =>  sender ! filterCountries(c => c.code.toLowerCase.equals(countryCode.toLowerCase))
    case CountryNameQuery(name) => sender ! filterCountries(c => c.name.toLowerCase.contains(name.toLowerCase))
    case _ => println(s"!!!! CountryQueryActor:: Something else"); throw new IllegalArgumentException("Not sure what happened...")
  }

  private def filterCountries(countryFilter: Country => Boolean) = {
    countries.filter(countryFilter) match {
      case found if found.length == 0 => None
      case found => Some(found)
    }
  }
}
