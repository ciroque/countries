package org.ciroque.countries.core

import org.joda.time.DateTime
import spray.json._

object MethodTiming extends DefaultJsonProtocol {
  def apply[T](name: String, logger: Logger = new LogWriter)(fx: T): T = {
    val start = DateTime.now
    val result: T = fx
    val end = DateTime.now
    val duration = end.minus(start.getMillis).getMillis
    logger.debug(new MethodTiming(name, start, end, duration).toJson.toString())
    result
  }

  implicit object DateTimeFormatter extends RootJsonFormat[DateTime] {
    override def read(json: JsValue): DateTime = DateTime.parse(json.toString())
    override def write(obj: DateTime): JsValue = JsString(obj.toDateTimeISO.toString())
  }

  implicit val MethodTimingFormat = jsonFormat4(MethodTiming.apply)

}

case class MethodTiming(name: String, start: DateTime, end: DateTime, duration: Long)
