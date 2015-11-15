package org.ciroque.countries

import akka.actor.ActorSystem
import akka.testkit.{TestKitBase, ImplicitSender, TestKit}
import org.scalatest.{FunSpec, BeforeAndAfterAll, Matchers, WordSpecLike}

class PingPongActorSpec(_system: ActorSystem)
  extends FunSpec
  with TestKitBase
  with ImplicitSender
  with Matchers
  with BeforeAndAfterAll {

  implicit lazy val system: ActorSystem = ActorSystem("CountryQueryActorTestingSystem")

  def this() = this(ActorSystem("MySpec"))

  override def afterAll() {
    TestKit.shutdownActorSystem(system)
  }

  describe("A Ping actor") {
    it("send back a pong message with the text 'pong' by default") {
      val pingActor = system.actorOf(PingActor.props)
      pingActor ! None
      expectMsg(PingActor.PongMessage("pong"))
    }

    it("send back a pong message with the given text") {
      val message = "this is the message"
      val pingActor = system.actorOf(PingActor.props)
      pingActor ! PingActor.PingMessage(message)
      expectMsg(PingActor.PongMessage(message))
    }
  }
}
