package models

import java.util.Date

import play.api.db._
import play.api.Play.current
import anorm._
import anorm.SqlParser._
import java.util.Date

case class Question(id: Option[Int],
                title: String,
                choiceA: String,
                choiceB: String,
                authorId: Int,
                totalA: Int,
                totalB: Int,
                creationDate: Date)

object Question {
  val question = {
    get[Option[Int]]("id") ~
    get[String]("title") ~
      get[String]("choice_A") ~
      get[String]("choice_B") ~
      get[Int]("author_id") ~
      get[Int]("total_A") ~
      get[Int]("total_B") ~
    get[Date]("creation_date") map {
      case id ~ title ~ choice_A ~ choice_B ~ author_id ~ total_A ~ total_B ~ creation_date =>
        Question(id, title,choice_A,choice_B, author_id, total_A, total_B, creation_date)
    }
  }
  def insert(newQuestion: Question) = {
    DB.withConnection { implicit connection =>
      SQL(
        """
            INSERT INTO questions(title, choice_A, choice_B, author_id, total_A, total_B, creation_date)
            VALUES ({title}, {choice_A}, {choice_B}, {author_id}, {total_A}, {total_B}, {creation_date})
        """
      ).on(
          'title -> newQuestion.title,
          'choice_A -> newQuestion.choiceA,
          'choice_B -> newQuestion.choiceB,
          'author_id -> newQuestion.authorId,
          'total_A -> newQuestion.totalA,
          'total_B -> newQuestion.totalB,
          'creation_date -> newQuestion.creationDate
        ).executeUpdate()
    }
  }
  def all(): List[Question] = DB.withConnection { implicit connection =>
    SQL("SELECT * FROM questions").as(question *)
  }

  def getOne(): Option[Question] = DB.withConnection { implicit connection =>
    SQL("SELECT * FROM questions ORDER BY RAND() LIMIT 1").as(question *).headOption
  }
}