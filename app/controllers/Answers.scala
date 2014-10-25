package controllers

import play.api.mvc._
import play.api._
import play.api.data._
import play.api.data.Forms._
import play.api.data.validation.Constraints._
import java.util.Calendar

import models._
import actions._

case class AnwserData(choice: String)

object Answers extends Controller {
  val Home = Application.Home

  val answerForm = Form(
    mapping(
      "choice" -> text.verifying(pattern("""[AB]""".r, error="Invalid answer (need to be A or B"))
    )(AnwserData.apply)(AnwserData.unapply)
  )
  def save(questionId: Long) = UserAuthAction { implicit request =>
    answerForm.bindFromRequest.fold(
      formWithErrors => Home.flashing("error" -> "Invalid answer"),
      answerDate => {
        request.session.get("userId").map { userId =>
          Answer.insert(Answer(questionId, userId.toLong, Some(answerDate.choice.head), Calendar.getInstance().getTime()))
          Home.flashing("success" -> "Question has been answered")
        }.getOrElse {
          Home.flashing("error" -> "Error to register the answer")
        }
      }
    )
  }
}
