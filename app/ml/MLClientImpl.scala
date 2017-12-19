package ml
import models.Trip
import play.api.libs.ws.{WSClient, WSRequest}
import com.typesafe.config.Config
import play.api.Logger
import play.api.http.ContentTypes
import play.api.http.HeaderNames.{ACCEPT, CONTENT_TYPE}
import play.api.http.HttpVerbs.POST
import play.api.libs.json.{JsError, JsSuccess, Json}

import scala.concurrent.Future
import scala.concurrent.duration._

class MLClientImpl(wsClient: WSClient, config: Config, logger: Logger)
    extends MLClient {

  val baseUrl = config.getString("ml-client-url")
  val timeout = config.getLong("timeout")
  override def rank5Trips(payload: Seq[Trip]): Future[Seq[String]] = {
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
        response.json.validate[Seq[String]] match {
          case JsSuccess(value, _) => value
          case JsError(err) =>
            logger.error(
              s"Error when trying to get response from ML service:\n${err}"
            )
            throw new RuntimeException("ML CLient response invalid")
        }
      }

  }
}
