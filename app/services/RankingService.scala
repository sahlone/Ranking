package services

import models.Trip

import scala.concurrent.Future
trait RankingService {

  def rankTrips(trips: Seq[Trip]): Future[Seq[String]]
}
