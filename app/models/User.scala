package models

import java.util.Date

import play.api.db._
import play.api.Play.current
import anorm._
import anorm.SqlParser._
import java.io.File

case class User(id: Option[Int],
                 registration_date: Date)

object User {
  val user = {
    get[Option[Int]]("id") ~
    get[Date]("registration_date") map {
      case id ~ registration_date =>
        User(id, registration_date)
    }
  }
  def insert(newUser: User) = {
    DB.withConnection { implicit connection =>
          SQL(
            """
            INSERT INTO users(registration_date)
            VALUES ({registration_date})
            """
          ).on(
              'registration_date -> newUser.registration_date
            ).executeUpdate()
    }
  }
}