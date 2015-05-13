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
    "send back a ping on a pong" in {
      val pingActor = system.actorOf(PingActor.props)
      pingActor ! PingActor.EmptyMessage()
      expectMsg(PingActor.PongMessage("pong"))
    }
  }
}
