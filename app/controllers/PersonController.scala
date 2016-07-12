package controllers

import javax.inject._

import com.eclipsesource.schema._
import play.api.Application
import play.api.libs.json.{JsError, JsSuccess, Json, Reads}
import play.api.mvc.{Action, Controller}
import services.Counter

import scala.collection.mutable.ListBuffer
import scala.concurrent.ExecutionContext


@Singleton
class PersonController @Inject()(app: Provider[Application], counter: Counter)(implicit ec: ExecutionContext) extends Controller {

  import models.Models._

  implicit lazy val application = app.get()
  val repo = ListBuffer[Person]()
  val validator = new SchemaValidator

  def all = Action {
    Ok(Json.toJson(repo.map(Json.toJson(_))))
  }

  def add = Action(parse.json) { request =>
    application.resourceAsStream("/public/schemas/person-schema.json").map(inputStream => {
      val validationResult = for {
        schema <- JsonSource.schemaFromStream(inputStream)
        result <- validator.validate(schema, request.body, personFormat: Reads[Person])
      } yield result

      validationResult match {
        case JsSuccess(person, _) =>
          person.copy(id = Some(counter.nextCount()))
          Ok("Person added")
        case JsError(errors) =>
          BadRequest(errors.toJson)
      }
    }).getOrElse(BadRequest("Could not find schema"))
  }

  def remove(id: Int) = Action {
    repo.find(_.id.contains(id))
      .map(person => {
        repo -= person
        Ok("Person removed")
      }).getOrElse(BadRequest("Person not found"))
  }
}
