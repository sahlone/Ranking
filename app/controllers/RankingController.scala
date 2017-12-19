package controllers

import javax.inject.Inject

import play.api.mvc.{ControllerComponents}
import services.RankingService
import play.api.mvc._
import play.api.libs.json._
import scala.concurrent.{ExecutionContext, Future}
import models.Trip
import formatters.JsonFormatter._
import org.slf4j.Logger

import scala.util.control.NonFatal
class RankingController @Inject()(rankingService: RankingService,
                                  controllerComponents: ControllerComponents,
                                  logger: Logger)(implicit ec: ExecutionContext)
    extends AbstractController(controllerComponents) {

  def rank = Action.async(parse.json) { request =>
    val jsResult = request.body.validate[Seq[Trip]]
    jsResult.fold(
      errors => {
        Future.successful(BadRequest("Unsupported request body"))
      },
      trips => {
        rankingService
          .rankTrips(trips)
          .map(seqTrips => Results.Ok(Json.toJson(seqTrips)))
          .recover {
            case NonFatal(ex) =>
              logger.error(s"Error in processing ranking : $trips : \n $ex")
              Results.InternalServerError("Please try after sometime")
          }

      }
    )
  }
}
