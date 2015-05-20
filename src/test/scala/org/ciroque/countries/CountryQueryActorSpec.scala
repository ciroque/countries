package org.ciroque.countries

import akka.actor.{Props, ActorSystem}
import akka.testkit.{ImplicitSender, TestKit}
import org.ciroque.countries.model.Country
import org.ciroque.countries.queries.{CountryCodeQuery, EmptyQuery}
import org.ciroque.countries.responses.CountryResponse
import org.scalatest._

import scala.concurrent.duration._
import scala.language.postfixOps

class CountryQueryActorSpec extends TestKit(ActorSystem("CountryQueryActorTestingSystem"))
with WordSpecLike
with Matchers
with BeforeAndAfterAll
with ImplicitSender {

  val noCountries = List()

  val countryA = new Country("AA", 1.1, 1.2, "A", "Country A")
  val countryB = new Country("BB", 2.1, 2.2, "B", "Country B")
  val countryC = new Country("CC", 3.1, 3.2, "C", "Country C")
  val countryD = new Country("DD", 4.1, 4.2, "D", "Country D")
  val countryE = new Country("EE", 5.1, 5.2, "E", "Country E")
  val countryF = new Country("FF", 6.1, 6.2, "F", "Country F")

  val allCountries = List(
    countryA,
    countryB,
    countryC,
    countryD,
    countryE,
    countryF
  )

  override def afterAll(): Unit = {
    shutdown()
  }

  "CountryQueryActor" should {
    "return None for list in the response when a None message is received" in {
      within(25 millis) {
        val templeDataQueryRef = system.actorOf(Props(classOf[CountryQueryActor], noCountries))
        templeDataQueryRef ! None
        expectMsg(new CountryResponse(None))
      }
    }

    "return all countries when an EmptyQuery is received" in {
      within(25 millis) {
        val templeDataQueryRef = system.actorOf(Props(classOf[CountryQueryActor], allCountries))
        templeDataQueryRef ! EmptyQuery
        expectMsg(new CountryResponse(Some(allCountries)))
      }
    }

    "return matching country when an CountryCodeQuery is received" in {
      within(25 millis) {
        val countryCode = "AA"
        val templeDataQueryRef = system.actorOf(Props(classOf[CountryQueryActor], allCountries))
        templeDataQueryRef ! CountryCodeQuery(countryCode)
        expectMsg(new CountryResponse(Some(List[Country](countryA))))
      }
    }

    "return an empty list when a CountryCodeQuery with a non-existent country code is received" in {
      within(25 millis) {
        val countryCode = "Z7"
        val templeDataQueryRef = system.actorOf(Props(classOf[CountryQueryActor], allCountries))
        templeDataQueryRef ! CountryCodeQuery(countryCode)
        expectMsg(new CountryResponse(Some(List())))
      }
    }
  }
}
