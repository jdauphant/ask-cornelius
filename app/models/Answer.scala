package models

import java.util.Date

import anorm.SqlParser._
import play.api.Play.current
import anorm._
import play.api.db.DB


case class Answer( questionId: Long,
                   userId: Long,
                   choice: Option[Char],
                   creationDate: Date)
{
  lazy val isA = choice.map { _ == 'A'}.getOrElse{false}
  lazy val isB = choice.map { _ == 'B'}.getOrElse{false}
  lazy val incA = if(isA) 1 else 0
  lazy val incB = if(isB) 1 else 0
}

object Answer {
  val answer = {
    get[Long]("user_id") ~
      get[Long]("question_id") ~
      get[Option[Char]]("choice") ~
      get[Date]("creation_date") map {
      case user_id ~ question_id ~ choice ~ registration_date =>
        Answer(user_id, question_id, choice, registration_date)
    }
  }
  def insert(newAnswer: Answer) = {
    DB.withTransaction { implicit connection =>
      SQL(
        """
            INSERT INTO answers (question_id, user_id,choice,creation_date)
            VALUES ({question_id}, {user_id}, {choice}, {creation_date})
        """
      ).on(
          'question_id -> newAnswer.questionId,
          'user_id -> newAnswer.userId,
          'choice -> newAnswer.choice,
          'creation_date -> newAnswer.creationDate
        ).executeInsert()
      SQL(
        """
            UPDATE questions SET total_A = total_A + {increment_total_A}, total_B = total_B + {increment_total_B}
            WHERE id = {question_id}
        """
      ).on('question_id -> newAnswer.questionId,
          'increment_total_A -> newAnswer.incA,
          'increment_total_B -> newAnswer.incB
        ).executeUpdate()
    }
  }
}