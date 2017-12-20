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
import common.MockMLService
class MockMLServiceModule(environment: Environment, configuration: Configuration)
    extends AbstractModule
    with ScalaModule
    with ArbitraryTypeReader
    with MockMLService{

  private implicit val executionContext = ExecutionContext.global

  override def configure() = {
    mockMLRankingService
  }

}
