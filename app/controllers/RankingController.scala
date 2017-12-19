package controllers

import javax.inject.Inject

import play.api.mvc.{Action, BaseController, ControllerComponents}
import services.RankingService
import play.api.mvc._
import play.api.libs.json._
import play.api.libs.functional.syntax._

import scala.concurrent.Future._
import scala.concurrent.{ExecutionContext, Future}
import models.Trip
import formatters.JsonFormatter._

import scala.util.Success
class RankingController @Inject()(
    rankingService: RankingService,
    controllerComponents: ControllerComponents)(implicit ec: ExecutionContext)
    extends AbstractController(controllerComponents) {

  def rank = Action.async(parse.json) { request =>
    val jsResult = request.body.validate[Seq[Trip]]
    jsResult.fold(
      errors => {
        Future.successful(BadRequest("Unsupported request body"))
      },
      trips => {
        rankingService.rankTrips(trips)

      }
    )
  }
}
