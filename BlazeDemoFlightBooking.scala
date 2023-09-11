package blazedemo

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.concurrent.duration._

class BlazeDemoFlightBooking extends Simulation {

  val httpProtocol = http
    .baseUrl("https://www.blazedemo.com")
    .header("Accept", "text/html")
    .header("User-Agent", "Mozilla/5.0")

  val scn = scenario("Book Flight Scenario")
    .exec(http("Load Homepage")
      .get("/"))
    .pause(1)
    .exec(http("Select Flight")
      .post("/reserve.php")
      .formParam("fromPort", "Paris")
      .formParam("toPort", "Buenos Aires"))
    .pause(1)
    .exec(http("Purchase Flight")
      .post("/purchase.php")
      .formParam("inputName", "John Doe")
      .formParam("address", "123 Main St")
      .formParam("city", "Anytown")
      .formParam("state", "CA")
      .formParam("zipCode", "12345")
      .formParam("creditCardNumber", "4111111111111111")
      .formParam("creditCardMonth", "12")
      .formParam("creditCardYear", "2023")
      .formParam("nameOnCard", "John Doe"))

  setUp(
    scn.inject(
      rampUsersPerSec(5) to 250 during (1 minute), // Teste de carga
      constantUsersPerSec(250) during (5 minutes) // Teste de pico
    )
  ).protocols(httpProtocol)
}