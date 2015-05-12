package org.ciroque.countries

import akka.actor.{Actor, ActorLogging, Props}

class PingActor
  extends Actor
  with ActorLogging {
  import org.ciroque.countries.PingActor._
  
  var counter = 0

  def receive = {
    case _ =>
      sender() ! PongMessage("pong")
  }	
}

object PingActor {
  val props = Props[PingActor]
  case object Initialize
  case class PongMessage(text: String)
  case class EmptyMessage()
}