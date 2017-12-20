package service
import ml.MLSeriviceImpl
import models.MLServiceConfig
import org.scalatest._
import org.slf4j.LoggerFactory
import play.api.libs.json._
import models.Trip
import play.api.BuiltInComponentsFromContext
import play.api.mvc.Results
import play.api.routing.Router
import play.api.test.WsTestClient
import play.core.server.Server
import play.filters.HttpFiltersComponents
import play.api.routing.sird._
import org.scalatestplus.play.PlaySpec
class MLServiceTest extends  PlaySpec
  with MustMatchers
  with EitherValues
  with OptionValues {

  implicit val ec = scala.concurrent.ExecutionContext.global
  val mockResult =
    """
      |["123","345","678"]
    """.stripMargin
  "ML Serivice : " must {

    "pass with 200 response" in {
      withValidMLServiceClient(_.rank5Trips(Seq.empty[Trip]).map(_ mustBe (Json.parse(mockResult).as[Seq[String]])))
    }

  }


  def withValidMLServiceClient[T](block: MLSeriviceImpl => T): T = {
    Server.withApplicationFromContext() { context =>
      new BuiltInComponentsFromContext(context) with HttpFiltersComponents {
        override def router: Router = Router.from {
          case POST(p"/ml") =>
            this.defaultActionBuilder { req =>
              req.body.asJson match {
                case Some(_) =>
                  Results.Created(Json.parse(mockResult))
                case _ =>
                  Results.BadRequest("")
              }
            }
        }
      }.application
    } { implicit port =>
      WsTestClient.withClient { client =>
        val mlServiceConf = MLServiceConfig(s"http://localhost:$port/ml", 10)
        block(
          new MLSeriviceImpl(client, mlServiceConf, LoggerFactory.getLogger("test-logger"))
        )
      }
    }
  }

}
