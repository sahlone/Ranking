package services

import javax.inject.Inject

import formatters.JsonFormatter._
import ml.MLSerivice
import models.Trip
import org.slf4j.{LoggerFactory, Logger}

import scala.concurrent.{ExecutionContext, Future}
import scala.util.control.NonFatal
/*
The algorithm is based on notion that if one of calls to ml service fails then we can say all of call failed
 */
class RankingServiceImpl @Inject()(mlService: MLSerivice, logger: Logger)(
    implicit ec: ExecutionContext)
    extends RankingService {

  override def rankTrips(trips: Seq[Trip]): Future[Seq[String]] = {

    val mlResult = trips
      .grouped(5)
      .toSeq
      .map(
        mlService.rank5Trips(_)
      )
    Future.sequence(mlResult).map(_.flatten)

  }
}
