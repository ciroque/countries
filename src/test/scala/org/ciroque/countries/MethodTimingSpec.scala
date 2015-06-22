package org.ciroque.countries

import org.ciroque.countries.core.{Logger, MethodTiming}
import org.scalatest.{FlatSpec, Matchers}

import scala.language.postfixOps

class MethodTimingSpec extends FlatSpec with Matchers {

  class MockLogger extends Logger {

    var entries: List[(String, String)] = List()

    def logEntry(level: String, entry: String) = {
      entries = entries :+ (level -> entry)
    }

    override def error(entry: String): Unit = logEntry("error", entry)
    override def warning(entry: String): Unit = logEntry("warning", entry)
    override def debug(entry: String): Unit = logEntry("debug", entry)
    override def info(entry: String): Unit = logEntry("info", entry)
  }

  it should "MethodTiming runs given function and times it..." in {
    val message: String = "This is in the MethodTiming execution"
    val functionToPerform = println(message)
    implicit val logger = new MockLogger
    MethodTiming("TESTING", logger) {
      functionToPerform
    }

    val entry = logger.entries.head
    entry._1 shouldEqual "debug"

    import spray.json._
    val methodTimingEntryFields = entry._2.parseJson.asJsObject.fields

    methodTimingEntryFields should contain key "name"
    methodTimingEntryFields.getOrElse("name", "") should be (JsString("TESTING"))
    methodTimingEntryFields should contain key "start"
    methodTimingEntryFields should contain key "end"
    methodTimingEntryFields should contain key "duration"
  }
}
