package controllers

import play.api._
import play.api.mvc._

object Application extends Controller {
  val Home = Redirect(routes.Questions.index())

  def index = Action {
    Home
  }

}