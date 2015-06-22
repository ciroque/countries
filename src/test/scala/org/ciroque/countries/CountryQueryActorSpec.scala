package org.ciroque.countries

import akka.actor.{ActorSystem, Props}
import akka.testkit.{ImplicitSender, TestKit}
import org.ciroque.countries.model.{Country, ParentGeoNode}
import org.ciroque.countries.queries.{CountryCodesQuery, CountryCodeQuery, CountryNameQuery, EmptyQuery}
import org.scalatest._

import scala.concurrent.duration._
import scala.language.postfixOps

class CountryQueryActorSpec extends TestKit(ActorSystem("CountryQueryActorTestingSystem"))
  with WordSpecLike
  with Matchers
  with BeforeAndAfterAll
  with ImplicitSender {

  val CALL_TIMEOUT = 120 millis

  val noCountries = List()

  val countryA = new Country("AA", "Country A", "Country A Full Name", ParentGeoNode.Empty)
  val countryB = new Country("BB", "Country B", "Country B Full Name", ParentGeoNode.Origin)
  val countryC = new Country("CC", "Country C", "Country C Full Name", ParentGeoNode.Empty)
  val countryD = new Country("DD", "Country D", "Country D Full Name", ParentGeoNode.Origin)
  val countryE = new Country("EE", "Country E", "Country E Full Name", ParentGeoNode.Empty)
  val countryF = new Country("FF", "Odd Name", "Country F Full Name", ParentGeoNode.Origin)

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
        expectMsg(None)
      }
    }

    "return all countries when an EmptyQuery is received" in {
      val templeDataQueryRef = system.actorOf(Props(classOf[CountryQueryActor], allCountries))
      within(CALL_TIMEOUT) {
        templeDataQueryRef ! new EmptyQuery()
        expectMsg(Some(allCountries))
      }
    }

    "return matching country when an CountryCodeQuery is received" in {
      val countryCode = "AA"
      val templeDataQueryRef = system.actorOf(Props(classOf[CountryQueryActor], allCountries))
      within(CALL_TIMEOUT) {
        templeDataQueryRef ! CountryCodeQuery(countryCode)
        expectMsg(Some(List[Country](countryA)))
      }
    }

    "should perform CountryCodeQuery in a case-insensitive manner" in {
      val countryCode = "aA"
      val templeDataQueryRef = system.actorOf(Props(classOf[CountryQueryActor], allCountries))
      within(CALL_TIMEOUT) {
        templeDataQueryRef ! CountryCodeQuery(countryCode)
        expectMsg(Some(List[Country](countryA)))
      }
    }

    "return correct results with CountryNameQuery" in {
      val expected = Some(List(countryA, countryB, countryC, countryD, countryE))
      val name = "Country"
      val templeDataQueryRef = system.actorOf(Props(classOf[CountryQueryActor], allCountries))
      within(CALL_TIMEOUT) {
        templeDataQueryRef ! CountryNameQuery(name)
        expectMsg(expected)
      }
    }

    "return correct results with CountryNameQuery in a case-insensitive manner" in {
      val expected = Some(List(countryA, countryB, countryC, countryD, countryE))
      val name = "country"
      val templeDataQueryRef = system.actorOf(Props(classOf[CountryQueryActor], allCountries))
      within(CALL_TIMEOUT) {
        templeDataQueryRef ! CountryNameQuery(name)
        expectMsg(expected)
      }
    }

    "return None when a CountryCodeQuery with a non-existent country code is received" in {
      val countryCode = "Z7"
      val templeDataQueryRef = system.actorOf(Props(classOf[CountryQueryActor], allCountries))
      within(CALL_TIMEOUT) {
        templeDataQueryRef ! CountryCodeQuery(countryCode)
        expectMsg(None)
      }
    }

    "return None when a CountryNameQuery with a non-matching country name is received" in {
      val countryName = "Kowabunga"
      val templeDataQueryRef = system.actorOf(Props(classOf[CountryQueryActor], allCountries))
      within(CALL_TIMEOUT) {
        templeDataQueryRef ! CountryNameQuery(countryName)
        expectMsg(None)
      }
    }

    "return the correct list of countries given a list of country codes" in {
      val countryCodes = List("AA", "CC", "DD", "FF")
      val templeDataQueryRef = system.actorOf(Props(classOf[CountryQueryActor], allCountries))
      val expected = Some(List(countryA, countryC, countryD, countryF))
      within(CALL_TIMEOUT) {
        templeDataQueryRef ! CountryCodesQuery(countryCodes)
        expectMsg(expected)
      }
    }
  }
}
