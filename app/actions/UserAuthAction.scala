package actions

import java.util.Calendar

import play.api.Logger
import play.api._
import play.api.mvc._
import scala.concurrent.Future
import play.api.libs.concurrent.Execution.Implicits.defaultContext

import models._
import controllers._

object UserAuthAction extends ActionBuilder[Request] {
  def invokeBlock[A](request: Request[A], block: (Request[A]) => Future[Result]) = {
    request.session.get("userId") match {
      case None =>
        User.insert(User(None,Calendar.getInstance().getTime())).map { id =>
          Future.successful(Application.Home.withSession( request.session + ("userId" -> id.toString)))
        }.getOrElse {
          Logger.warn("Impossible to create an user id")
          Future.successful(Application.Home)
        }
      case Some(u) => block(request)

    }

  }
}