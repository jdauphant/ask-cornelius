package controllers

import play.api.mvc._
import play.api._
import views._

object Questions extends Controller {
  def index() = Action { implicit request =>
    Ok(views.html.Questions.index("test"))
  }
}
