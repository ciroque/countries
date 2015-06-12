package org.ciroque.countries

import org.ciroque.countries.model.Country
import spray.json.JsonParser.ParsingException
import spray.json._

import scala.io.Source

class CountryDataFileReader(filename: String) extends CountryDataLoader {
  override def load(): Option[List[Country]] = {
    try {
      val resource = getClass.getResource(filename)
      val source = Source.fromURL(resource)
      val fileContent = source.getLines().mkString
      val parsed = fileContent.parseJson

      import Country._
      Some(parsed.convertTo[List[Country]])

    } catch {
      case pex: ParsingException =>
        println(pex)
        None
    }
  }
}
