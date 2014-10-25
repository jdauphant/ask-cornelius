package controllers

import play.api.mvc._
import play.api._
import play.api.data._
import play.api.data.Forms._
import play.api.data.validation.Constraints._
import java.util.Calendar

import views._
import models._

case class QuestionData(title: String, choiceA: String, choiceB: String)

object Questions extends Controller {
  val Home = Application.Home

  val questionForm = Form(
    mapping(
      "title" -> nonEmptyText,
      "choiceA" -> text,
      "choiceB" -> text
    )(QuestionData.apply)(QuestionData.unapply)
  )

  def questionDataToQuestion(question: QuestionData) = question match {
    case QuestionData(title,choiceA,choiceB) =>
      Question(None,title,choiceA,choiceB,1,0,0,Calendar.getInstance().getTime())
  }

  def index() = Action { implicit request =>
    Ok(views.html.Questions.index(Question.getOne()))
  }

  def create() = Action { implicit request =>
    Ok(views.html.Questions.create(questionForm))
  }

  def save = Action { implicit request =>
    questionForm.bindFromRequest.fold(
      formWithErrors => BadRequest(html.Questions.create(formWithErrors)),
      question => {
        Question.insert(questionDataToQuestion(question))
        Home.flashing("success" -> "Question has been created")
      }
    )
  }
}
