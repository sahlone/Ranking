package ml

import models.{MLServiceConfig, Trip}
import play.api.libs.ws._
import org.slf4j.{Logger}
import play.api.http.ContentTypes
import play.api.http.HeaderNames.{ACCEPT, CONTENT_TYPE}
import play.api.http.HttpVerbs.POST
import play.api.libs.json.{JsError, JsSuccess, Json}
import scala.concurrent.{ExecutionContext, Future}
import scala.concurrent.duration._
import formatters.JsonFormatter._
import scala.util.Try
import scala.util.control.NonFatal

class MLSeriviceImpl(wsClient: WSClient,
                     config: MLServiceConfig,
                     logger: Logger)(implicit ex: ExecutionContext)
    extends MLSerivice {

  lazy val baseUrl = config.serviceEndpoint
  lazy val timeout = config.timeout

  override def rank5Trips(payload: Seq[Trip]): Future[Seq[String]] = {

    Try {
      logger.debug(s"Base URL : $baseUrl")
      val httpRequest: WSRequest = wsClient
        .url(baseUrl)
        .withRequestTimeout(timeout seconds)
        .withHttpHeaders(
          ACCEPT -> ContentTypes.JSON,
          CONTENT_TYPE -> ContentTypes.JSON
        )
        .withMethod(POST)
        .withBody(Json.toJson(payload))

      httpRequest
        .execute()
        .map { response =>
          logger.debug(s"Remote Response ML Service response : $response")
          response.status match {
            case 200 =>response.json.validate[Seq[String]] match {
              case JsSuccess(value, _) => value
              case JsError(err) =>
                logger.error(
                  s"Error when trying to get response from ML service:\n${err}"
                )
                throw new RuntimeException("ML CLient response invalid")
            }
            case _ => throw new RuntimeException(s"ML CLient response invalid ${response.body}")
          }

        }
    }.recoverWith {
      case NonFatal(e) => Try { Future.failed(e) }
    }.get

  }
}
