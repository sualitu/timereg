package dk.sual.timereg.model

import dk.sual.timereg.db.DbAccess
import dk.sual.timereg.Helpers.Util._

import java.util.UUID
import scala.slick.driver.MySQLDriver.simple._

object Session extends DbAccess {
  /**
    * Creates a session
    *
    * @param user - the id of the user of the session
    * @return A session
    */
  def create(user: Int): Session = Db withSession { implicit session =>
    val s = Session(UUID.randomUUID().toString(), user)
    sessions insert s

    s
  }

  /**
    * Gets a session for a given user id
    * 
    * @param user - the id of the user
    * @return Maybe a session
    */
  def fromUser(user: Int) : Option[Session] = Db withSession { implicit session =>
    val se = for {
      s <- sessions if s.userId === user
    } yield s
    uniqueMatch(se run)
  }

  /**
    * Invalidates a session by deleting it.
    * 
    * @param key - the key of the session
    */
  def invalidate(key: UUID) = Db withSession { implicit session =>
    val se = for {
      s <- sessions if s.key === key.toString()
    } yield s

    se delete
  }

  /**
    * Gets a session from a key
    * 
    * @param the key
    * @return Maybe a session
    */
  def fromKey(key: UUID) : Option[Session] = Db withSession { implicit session =>
    val se = for {
      s <- sessions if s.key === key.toString()
    } yield s
    uniqueMatch(se run)
  }
}

case class Session(
  private val _key: String,
  private val _user: Int) {

  /**
    * The session key
    */
  val key = UUID.fromString(_key)

  /**
    * Invalidates this session
    */
  def invalidate = Session invalidate(key)

  /**
    * The user of this session
    */
  val user: Option[User] = User fromId(_user)
}
