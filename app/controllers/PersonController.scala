package controllers

import javax.inject._

import com.eclipsesource.schema._
import play.api.Environment
import play.api.libs.json._
import play.api.mvc._
import services.Counter

import scala.collection.mutable.ListBuffer
import scala.concurrent.ExecutionContext


@Singleton
class PersonController @Inject()(
                                  val controllerComponents: ControllerComponents,
                                  environment: Environment,
                                  counter: Counter)(implicit ec: ExecutionContext)
  extends BaseController {

  import models.Models._

  private val repo = ListBuffer[Person]()
  private val validator = new SchemaValidator

  def index = Action {
    Ok(views.html.index())
  }

  def all: Action[AnyContent] = Action {
    Ok(Json.toJson(repo.map(Json.toJson(_))))
  }

  def add: Action[JsValue] = Action(parse.json) { request: Request[JsValue] =>
    environment.resourceAsStream("/public/schemas/person-schema.json").map(inputStream => {
      val validationResult = for {
        schema <- JsonSource.schemaFromStream(inputStream)
        result <- validator.validate(schema, request.body, personFormat: Reads[Person])
      } yield result

      validationResult match {
        case JsSuccess(person, _) =>
          repo += person.copy(id = Some(counter.nextCount()))
          Ok("Person added")
        case JsError(errors) =>
          BadRequest(errors.toJson)
      }
    }).getOrElse(BadRequest("Could not find schema"))
  }

  def remove(id: Int) = Action {
    repo
      .find(_.id.contains(id))
      .map(person => {
        repo -= person
        Ok("Person removed")
      }).getOrElse(BadRequest("Person not found"))
  }
}
