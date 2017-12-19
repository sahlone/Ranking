package formatters

import play.api.libs.json.{JsPath, Reads}
import models.Trip
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import play.api.libs.json._
import play.api.libs.functional.syntax._
object JsonFormatter {

  implicit val tripReads: Reads[Trip] = (
    (JsPath \ "tripId").read[String] and
      (JsPath \ "departure").read[Long] and
      (JsPath \ "tripTime").read[Float] and
      (JsPath \ "noOfConnections").read[Int] and
      (JsPath \ "tripDistance").read[Float] and
      (JsPath \ "tripPrice").read[Float]
  )(Trip.apply _)

  implicit val tripWrites: Writes[Trip] = (
    (JsPath \ "tripId").write[String] and
      (JsPath \ "departure").write[Long] and
      (JsPath \ "tripTime").write[Float] and
      (JsPath \ "noOfConnections").write[Int] and
      (JsPath \ "tripDistance").write[Float] and
      (JsPath \ "tripPrice").write[Float]
  )(unlift(Trip.unapply))

}
