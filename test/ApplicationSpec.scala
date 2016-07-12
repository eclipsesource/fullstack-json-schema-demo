
import org.scalatestplus.play._
import play.api.libs.json.Json
import play.api.test._
import play.api.test.Helpers._

/**
 * Add your spec here.
 * You can mock out a whole application including requests, plugins etc.
 * For more information, consult the wiki.
 */
class ApplicationSpec extends PlaySpec with OneAppPerTest {

  "Routes" should {
    "send 404 on a bad request" in  {
      route(app, FakeRequest(GET, "/boum")).map(status(_)) mustBe Some(NOT_FOUND)
    }
  }

  "PersonController" should {

    import models.Models._

    "add a person" in {
      val person = Person(None, "Marilyn", 30)
      val personJson = Json.toJson(person)
      contentAsString(route(app, FakeRequest(POST, "/person").withJsonBody(personJson)).get) mustBe "Person added"
    }
  }
}
