package models

import play.api.libs.json.Json

object Models {

  implicit val personFormat = Json.format[Person]
  case class Person(id: Option[Int], name: String, age: Int)

}
