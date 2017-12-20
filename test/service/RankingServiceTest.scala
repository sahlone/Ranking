package service

import org.scalatest.AsyncWordSpec
import org.scalatest._
import org.scalatest.mockito.MockitoSugar
import scala.concurrent.{ExecutionContext, Future}
import services.{RankingService, RankingServiceImpl}
import ml.MLSerivice
import org.mockito.Mockito._
import org.mockito.Matchers._
import org.slf4j.LoggerFactory
import models.Trip

class RankingServiceTest
    extends AsyncWordSpec
    with MustMatchers
    with EitherValues
    with OptionValues
    with MockitoSugar {

  implicit val ec = ExecutionContext.global
  val five = Seq("1", "2", "3", "4", "5")
  val trip = Trip("id", 1234353l, 12.3f, 1, 12.3f, 32.1f)
  "Ranking service " must {

    "contain all trip Ids in response " in {
      withRankingService(false)(
        _.rankTrips(Seq(trip)).map(_.size mustBe (5))
      )
    }

    "fail  processing ranks " in {
      recoverToSucceededIf[RuntimeException](
        withRankingService(true)(_.rankTrips(Seq(trip))))
    }
  }

  def withRankingService[T](fail: Boolean)(block: RankingService => T) = {
    val mockMLService = mock[MLSerivice]

    val rankingService =
      new RankingServiceImpl(mockMLService, LoggerFactory.getLogger("test"))
    fail match {
      case false =>
        when(mockMLService.rank5Trips(any()))
          .thenReturn(Future.successful(five))
      case true =>
        when(mockMLService.rank5Trips(any()))
          .thenReturn(Future.failed(new RuntimeException))
    }
    block(rankingService)
  }
}
