package ml

import models.Trip

import scala.concurrent.Future
trait MLClient {

  def rank5Trips(payload: Seq[Trip]): Future[Seq[String]]
}
