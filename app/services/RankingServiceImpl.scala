package services

import javax.inject.Inject

import formatters.JsonFormatter._
import ml.MLClientImpl
import models.Trip
import play.api.Logger

import scala.concurrent.Future
import scala.util.control.NonFatal

class RankingServiceImpl @Inject()(mlService: MLClientImpl, logger: Logger)
    extends RankingService {

  override def rankTrips(trips: Seq[Trip]): Future[Seq[String]] = {

    val sequence: Future[Seq[Seq[String]]] = Future
      .sequence(
        trips
          .grouped(4)
          .toList
          .foldLeft(Seq.empty[Future[Seq[String]]])((aggr, trips) => {
            aggr :+ mlService.rank5Trips(trips)
          }))

    sequence.recoverWith {
      case NonFatal(ex) =>
        logger.error(s"Error while getting results from ML service $ex")
        Future.successful(Seq.empty[String])
    }

  }
}
