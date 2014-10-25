package controllers

import play.api.mvc._
import play.api._
import play.api.data._
import play.api.data.Forms._
import play.api.data.validation.Constraints._
import java.util.Calendar

import views._
import models._
import actions._

case class QuestionData(title: String, choiceA: String, choiceB: String)

object Questions extends Controller {
  val Home = Application.Home

  def index() = UserAuthAction { implicit request =>
    val question: Option[Question] = request.session.get("userId").map { userId => Question.getOne(userId.toLong) }.
      getOrElse{ None }
    Ok(views.html.Questions.index(question))
  }

  def questionDataToQuestion(question: QuestionData, authorId: Int) = question match {
    case QuestionData(title,choiceA,choiceB) =>
      Question(None,title,choiceA,choiceB,authorId,0,0,Calendar.getInstance().getTime())
  }
  val questionForm = Form(
    mapping(
      "title" -> nonEmptyText(3,140),
      "choiceA" -> nonEmptyText(1,15),
      "choiceB" -> nonEmptyText(1,15)
    )(QuestionData.apply)(QuestionData.unapply)
  )

  def create() = UserAuthAction { implicit request =>
    Ok(views.html.Questions.create(questionForm))
  }

  def list() = UserAuthAction { implicit request =>
    val userId = request.session.get("userId")
    val myQuestions: List[Question] = userId.map { userId => Question.allFromAuthor(userId.toLong) }.
      getOrElse{ Nil }
    val myAnsweredQuestions: List[Question] = userId.map { userId => Question.allFromRespondent(userId.toLong) }.
      getOrElse{ Nil }
    Ok(views.html.Questions.list(myQuestions, myAnsweredQuestions))
  }

  def save() = UserAuthAction { implicit request =>
    questionForm.bindFromRequest.fold(
      formWithErrors => BadRequest(html.Questions.create(formWithErrors)),
      question => {
        request.session.get("userId").map { userId =>
          Question.insert(questionDataToQuestion(question,userId.toInt))
          Home.flashing("success" -> "Question has been created")
        }.getOrElse {
          Home.flashing("error" -> "Question was not created")
        }
      }
    )
  }
}
