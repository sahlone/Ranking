package common

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock._
import com.github.tomakehurst.wiremock.core.WireMockConfiguration.options
import com.github.tomakehurst.wiremock.stubbing.StubMapping

trait MockMLService {


  val response =
    """
      |[
      |"134","123","342"
      |]
    """.stripMargin
  val wireMockServer = new WireMockServer(options().port(8000));
  wireMockServer.start


  def stopWireMockServer = wireMockServer.stop

  def mockMLRankingService = {
    wireMockServer.stubFor(
      post(urlEqualTo("/ml"))
        .willReturn(
          aResponse()
            .withHeader("Content-Type", "text/plain")
            .withBody(response)))
  }
}
