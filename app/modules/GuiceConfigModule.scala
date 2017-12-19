package modules

import javax.inject.Singleton
import net.ceedubs.ficus.readers.ArbitraryTypeReader
import com.google.inject.{AbstractModule, Provides}
import net.codingwell.scalaguice.ScalaModule
import play.api.{Configuration, Environment}
import play.api.mvc.ControllerComponents
import services._

import scala.concurrent.ExecutionContext

class GuiceConfigModule(environment: Environment, configuration: Configuration)
    extends AbstractModule
    with ScalaModule
    with ArbitraryTypeReader {

  override def configure() = {}
  @Provides
  @Singleton
  def providesRankingSerice(
      controllerComponents: ControllerComponents,
      executionContext: ExecutionContext
  ): RankingService =
    new RankingServiceImpl()

}
