package org.ciroque.countries.model

case class ParentGeoNode(centroid: GeoNode)
case class GeoNode(`type`: String, coordinates: List[Double])
case class Country(code: String, name: String, fullName: String, geo: ParentGeoNode)

object GeoNode extends spray.json.DefaultJsonProtocol {
  def Empty: GeoNode = new GeoNode("Feature", List())
  def Origin: GeoNode = new GeoNode("Feature", List(0.0,0.0))
  implicit val GeoNodeProtocol = jsonFormat2(GeoNode.apply)
}

object ParentGeoNode extends spray.json.DefaultJsonProtocol {
  def Empty: ParentGeoNode = new ParentGeoNode(GeoNode.Empty)
  def Origin: ParentGeoNode = new ParentGeoNode(GeoNode.Origin)
  implicit val ParentGeoNodeProtocol = jsonFormat1(ParentGeoNode.apply)
}

object Country extends spray.json.DefaultJsonProtocol {
  def Empty: Country = new Country(null, null, null, null)
  implicit val CountryProtocol = jsonFormat4(Country.apply)
}

