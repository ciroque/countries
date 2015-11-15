package org.ciroque.countries

import akka.actor.{ActorSystem, Props}
import akka.testkit.{ImplicitSender, TestKitBase}
import org.ciroque.countries.model.{Country, ParentGeoNode}
import org.ciroque.countries.queries.{CountryCodeQuery, CountryCodesQuery, CountryNameQuery, EmptyQuery}
import org.scalatest._

import scala.concurrent.duration._
import scala.language.postfixOps

class CountryQueryActorSpec extends FunSpec
  with TestKitBase
  with Matchers
  with BeforeAndAfterAll
  with ImplicitSender {

  implicit lazy val system: ActorSystem = ActorSystem("CountryQueryActorTestingSystem")

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

  describe("CountryQueryActor") {
    it("return None for list in the response when a None message is received") {
      val templeDataQueryRef = system.actorOf(Props(classOf[CountryQueryActor], noCountries))
      within(CALL_TIMEOUT) {
        templeDataQueryRef ! None
        expectMsg(None)
      }
    }

    it("return all countries when an EmptyQuery is received") {
      val templeDataQueryRef = system.actorOf(Props(classOf[CountryQueryActor], allCountries))
      within(CALL_TIMEOUT) {
        templeDataQueryRef ! new EmptyQuery()
        expectMsg(Some(allCountries))
      }
    }

    it("return matching country when an CountryCodeQuery is received") {
      val countryCode = "AA"
      val templeDataQueryRef = system.actorOf(Props(classOf[CountryQueryActor], allCountries))
      within(CALL_TIMEOUT) {
        templeDataQueryRef ! CountryCodeQuery(countryCode)
        expectMsg(Some(List[Country](countryA)))
      }
    }

    it("should perform CountryCodeQuery in a case-insensitive manner") {
      val countryCode = "aA"
      val templeDataQueryRef = system.actorOf(Props(classOf[CountryQueryActor], allCountries))
      within(CALL_TIMEOUT) {
        templeDataQueryRef ! CountryCodeQuery(countryCode)
        expectMsg(Some(List[Country](countryA)))
      }
    }

    it("return correct results with CountryNameQuery") {
      val expected = Some(List(countryA, countryB, countryC, countryD, countryE))
      val name = "Country"
      val templeDataQueryRef = system.actorOf(Props(classOf[CountryQueryActor], allCountries))
      within(CALL_TIMEOUT) {
        templeDataQueryRef ! CountryNameQuery(name)
        expectMsg(expected)
      }
    }

    it("return correct results with CountryNameQuery in a case-insensitive manner") {
      val expected = Some(List(countryA, countryB, countryC, countryD, countryE))
      val name = "country"
      val templeDataQueryRef = system.actorOf(Props(classOf[CountryQueryActor], allCountries))
      within(CALL_TIMEOUT) {
        templeDataQueryRef ! CountryNameQuery(name)
        expectMsg(expected)
      }
    }

    it("return None when a CountryCodeQuery with a non-existent country code is received") {
      val countryCode = "Z7"
      val templeDataQueryRef = system.actorOf(Props(classOf[CountryQueryActor], allCountries))
      within(CALL_TIMEOUT) {
        templeDataQueryRef ! CountryCodeQuery(countryCode)
        expectMsg(None)
      }
    }

    it("return None when a CountryNameQuery with a non-matching country name is received") {
      val countryName = "Kowabunga"
      val templeDataQueryRef = system.actorOf(Props(classOf[CountryQueryActor], allCountries))
      within(CALL_TIMEOUT) {
        templeDataQueryRef ! CountryNameQuery(countryName)
        expectMsg(None)
      }
    }

    it("return the correct list of countries given a list of country codes") {
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
