package modules

import javax.inject.Singleton

import net.ceedubs.ficus.readers.ArbitraryTypeReader
import com.google.inject.{AbstractModule, Provides}
import ml.{MLSerivice, MLSeriviceImpl}
import net.codingwell.scalaguice.ScalaModule
import play.api.libs.ws.WSClient
import play.api.mvc.ControllerComponents
import services._
import com.typesafe.config.{Config, ConfigFactory}
import models.MLServiceConfig
import org.slf4j.{Logger, LoggerFactory}
import play.api.{Configuration, Environment}
import net.ceedubs.ficus.Ficus._
import net.ceedubs.ficus.readers.ArbitraryTypeReader
import scala.concurrent.ExecutionContext

class GuiceConfigModule(environment: Environment, configuration: Configuration)
    extends AbstractModule
    with ScalaModule
    with ArbitraryTypeReader {

  private implicit val executionContext = ExecutionContext.global

  override def configure() = {}
  @Provides
  @Singleton
  def providesRankingService(
      mlService: MLSerivice,
      logger: Logger
  ): RankingService =
    new RankingServiceImpl(mlService, logger)(executionContext)

  @Provides
  @Singleton
  def providesMLService(wSClient: WSClient,
                        config: MLServiceConfig,
                        logger: Logger): MLSerivice =
    new MLSeriviceImpl(wSClient, config, logger)(executionContext)

  @Provides
  def provideSystemLogger: Logger = LoggerFactory.getLogger("ranking-service")

  @Provides
  @Singleton
  def provideMLServiceConfig: MLServiceConfig = {
    val conf = configuration.underlying
    conf.as[MLServiceConfig]("ranking-service.remoteRankingService")
  }

}
