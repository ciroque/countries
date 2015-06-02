package org.ciroque.countries

import akka.actor.{Props, ActorSystem}
import akka.testkit.{ImplicitSender, TestKit}
import org.ciroque.countries.model.Country
import org.ciroque.countries.queries.{CountryNameQuery, CountryCodeQuery, EmptyQuery}
import org.ciroque.countries.responses.CountryResponse
import org.scalatest._

import scala.concurrent.duration._
import scala.language.postfixOps

class CountryQueryActorSpec extends TestKit(ActorSystem("CountryQueryActorTestingSystem"))
  with WordSpecLike
  with Matchers
  with BeforeAndAfterAll
  with ImplicitSender {

  val CALL_TIMEOUT = 70 millis

  val noCountries = List()

  val countryA = new Country("AA", 1.1, 1.2, "Country A", "Country A Full Name")
  val countryB = new Country("BB", 2.1, 2.2, "Country B", "Country B Full Name")
  val countryC = new Country("CC", 3.1, 3.2, "Country C", "Country C Full Name")
  val countryD = new Country("DD", 4.1, 4.2, "Country D", "Country D Full Name")
  val countryE = new Country("EE", 5.1, 5.2, "Country E", "Country E Full Name")
  val countryF = new Country("FF", 6.1, 6.2, "Odd Name", "Country F Full Name")

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
      val templeDataQueryRef = system.actorOf(Props(classOf[CountryQueryActor], noCountries))
      within(CALL_TIMEOUT) {
        templeDataQueryRef ! None
        expectMsg(new CountryResponse(None))
      }
    }

    "return all countries when an EmptyQuery is received" in {
      val templeDataQueryRef = system.actorOf(Props(classOf[CountryQueryActor], allCountries))
      within(CALL_TIMEOUT) {
        templeDataQueryRef ! EmptyQuery
        expectMsg(new CountryResponse(Some(allCountries)))
      }
    }

    "return matching country when an CountryCodeQuery is received" in {
      val countryCode = "AA"
      val templeDataQueryRef = system.actorOf(Props(classOf[CountryQueryActor], allCountries))
      within(CALL_TIMEOUT) {
        templeDataQueryRef ! CountryCodeQuery(countryCode)
        expectMsg(new CountryResponse(Some(List[Country](countryA))))
      }
    }

    "should perform CountryCodeQuery in a case-insensitive manner" in {
      val countryCode = "aA"
      val templeDataQueryRef = system.actorOf(Props(classOf[CountryQueryActor], allCountries))
      within(CALL_TIMEOUT) {
        templeDataQueryRef ! CountryCodeQuery(countryCode)
        expectMsg(new CountryResponse(Some(List[Country](countryA))))
      }
    }

    "return correct results with CountryNameQuery" in {
      val expected = Some(List(countryA, countryB, countryC, countryD, countryE))
      val name = "Country"
      val templeDataQueryRef = system.actorOf(Props(classOf[CountryQueryActor], allCountries))
      within(CALL_TIMEOUT) {
        templeDataQueryRef ! CountryNameQuery(name)
        expectMsg(new CountryResponse(expected))
      }
    }

    "return correct results with CountryNameQuery in a case-insensitive manner" in {
      val expected = Some(List(countryA, countryB, countryC, countryD, countryE))
      val name = "country"
      val templeDataQueryRef = system.actorOf(Props(classOf[CountryQueryActor], allCountries))
      within(CALL_TIMEOUT) {
        templeDataQueryRef ! CountryNameQuery(name)
        expectMsg(new CountryResponse(expected))
      }
    }

    "return an empty list when a CountryCodeQuery with a non-existent country code is received" in {
      val countryCode = "Z7"
      val templeDataQueryRef = system.actorOf(Props(classOf[CountryQueryActor], allCountries))
      within(CALL_TIMEOUT) {
        templeDataQueryRef ! CountryCodeQuery(countryCode)
        expectMsg(new CountryResponse(Some(List())))
      }
    }
  }
}
