package models

import play.api.db._
import play.api.Play.current
import anorm._
import anorm.SqlParser._
import java.util.Date

case class Question(id: Option[Long],
                title: String,
                choiceA: String,
                choiceB: String,
                authorId: Long,
                totalA: Int,
                totalB: Int,
                creationDate: Date)
{
  def totalAll = totalA+totalB
  def ratioAPerCent() = totalAll match {
    case 0 => None
    case total => (totalA*100/total)
  }
  def ratioBPerCent() = totalAll match {
    case 0 => None
    case total => (totalB*100/total)
  }
}


object Question {
  val question = {
    get[Option[Long]]("id") ~
    get[String]("title") ~
      get[String]("choice_A") ~
      get[String]("choice_B") ~
      get[Long]("author_id") ~
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
        ).executeInsert()
    }
  }

  def all(): List[Question] = DB.withConnection { implicit connection =>
    SQL("SELECT * FROM questions").as(question *)
  }
  def allFromAuthor(authorId: Long, limit: Int = 100): List[Question] = DB.withConnection { implicit connection =>
    SQL("SELECT * FROM questions WHERE author_id = {author_id} ORDER BY creation_date DESC LIMIT "+limit).on('author_id -> authorId).as(question *)
  }
  def allFromRespondent(respondentId: Long, limit: Int = 100): List[Question] = DB.withConnection { implicit connection =>
    SQL("SELECT * FROM questions WHERE id IN (SELECT question_id FROM answers WHERE answers.user_id = {respondent_id} ) ORDER BY creation_date DESC LIMIT "+limit).on('respondent_id -> respondentId).as(question *)
  }
  def getOne(authorIdToExclude: Long): Option[Question] = DB.withConnection { implicit connection =>
    SQL("SELECT * FROM questions WHERE author_id <> {author_id} AND id NOT IN (SELECT question_id FROM answers WHERE answers.user_id = {author_id}) ORDER BY RAND() LIMIT 1").on('author_id -> authorIdToExclude).as(question *).headOption
  }
}