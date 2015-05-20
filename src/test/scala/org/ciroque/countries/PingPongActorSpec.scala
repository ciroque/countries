package org.ciroque.countries

import akka.actor.ActorSystem
import akka.testkit.{ImplicitSender, TestKit}
import org.scalatest.{BeforeAndAfterAll, Matchers, WordSpecLike}

class PingPongActorSpec(_system: ActorSystem)
  extends TestKit(_system)
  with ImplicitSender
  with WordSpecLike
  with Matchers
  with BeforeAndAfterAll {

  def this() = this(ActorSystem("MySpec"))

  override def afterAll() {
    TestKit.shutdownActorSystem(system)
  }

  "A Ping actor" must {
    "send back a pong message with the text 'pong' by default" in {
      val pingActor = system.actorOf(PingActor.props)
      pingActor ! None
      expectMsg(PingActor.PongMessage("pong"))
    }

    "send back a pong message with the given text" in {
      val message = "this is the message"
      val pingActor = system.actorOf(PingActor.props)
      pingActor ! PingActor.PingMessage(message)
      expectMsg(PingActor.PongMessage(message))
    }
  }
}
