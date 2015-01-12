package dk.sual.timereg.model

import dk.sual.timereg.db.DbAccess
import dk.sual.timereg.Helpers.Util._
import dk.sual.timereg.Security._

import java.util.UUID

import scala.slick.driver.MySQLDriver.simple._

/**
  * A user mapped from the database
  */
object User extends DbAccess {

  /**
    * Returns a user if a user with the given id exists.
    * 
    * @param id - the id of the wanted user
    * @return Maybe a user
    */
  def fromId(id: Int) : Option[User] = Db withSession { implicit session =>
    val us = for {
      u <- users if u.id === id
    } yield u
    uniqueMatch(us run)
  }

  /**
    * Returns a user if a user with the given session key exists.
    * 
    * @param key - the session key
    * @return Maybe a user
    */
  def fromSession(key: UUID) : Option[User] = Db withSession { implicit session =>
    Session fromKey(key) match {
      case Some(k) => k.user
      case onFailure => None
    }
  }

  /**
    * Returns a user with the given name if it exists
    * 
    * @param name - the name
    * @return Maybe a user
    */
  def fromName(name: String) : Option[User] = Db withSession { implicit session =>
    val us = for {
      u <- users if u.name === name
    } yield u
    uniqueMatch(us run)
  }

  /**
    * Creates a user if a user with the given name does not already exist
    * 
    * @param name - the name of the user
    * @param password - the password of the user
    * @return Maybe a user
    */
  def create(name: String, password: String) : Option[User] = Db withSession { implicit session =>
    val us = for {
      u <- users if u.name === name
    } yield u
    if (!us.exists.run) {
      val (hashedPw, salt) = hashPassword(password)
      val user = User withoutId(name, hashedPw, salt)
      val id = (users returning users.map(_.id)) += user
      fromId(id)
    } else {
      None
    }
  }

  /**
    * Creates a user with an invalid id. This is used for adding users to the database before their real id is known.
    * 
    * @param name - the name of the user
    * @param password - the password of the user
    * @param salt - the salt
    * @return The user
    */
  private def withoutId(name: String, password: String, salt: String) : User= {
    User(-1, name, password, salt)
  }

  /**
    * Gets a list of customers associated with the given user.
    * 
    * @param user - the user
    * @return A list of customers
    */
  def getCustomers(user: User) : List[Customer] = getCustomers(user.id)

  /**
    * Gets a list of customers associated with the given user id.
    * 
    * @param id - the user id
    * @return A list of customers
    */
  def getCustomers(id: Int) : List[Customer] = Db withSession { implicit session =>
    val cs = for {
      c <- customers if c.ownerId === id
    } yield c
    cs.list
  }

  /**
    * Gets a list of projects associated with the given user.
    * 
    * @param user - the user
    * @param A list of projects
    */
  def getProjects(user: User) : List[Project] = getProjects(user.id)

  /**
    * Gets a list of projects associated with the given user id.
    * 
    * @param id - the user id
    * @return A list of projects
    */
  def getProjects(id: Int) : List[Project] = Db withSession { implicit session =>
    val ps = for {
      p <- projects if p.ownerId === id
    } yield p
    ps.list 
  }

  /**
    * Gets a list of rates associated with the given user id.
    * 
    * @param id - the user id
    * @return A list of rates
    */
  def getRates(id: Int) : List[Rate] = Db withSession { implicit session =>
    val rs = for {
      r <- rates if r.userId === id
    } yield r
    rs.list
  }

  /**
    * Logs in a user if the given password matches is correct. The password is hashed before checking.
    * 
    * @param name - The name of the user
    * @param password - The password of the user
    * @return Maybe the user and the session
    */
  def login(name: String, password: String) : Option[(User, Session)] = Db withSession { implicit session =>
    val us = for {
      u <- users if u.name === name
    } yield u
    uniqueMatch(us.list) match {
      case Some(u) => {
        if (checkPassword(password, u)) {
          val s = Session create(u.id)
          Some((u, s))
        } else {
          None
        }
      }
      case None => None
    }
  }
}

case class User(
  val id: Int,
  private var _name: String,
  private var _password: String,
  private var _salt: String) {

  import User._

  /**
    * The user name
    */
  def name: String = _name

  /**
    * The user salt
    */
  def salt: String = _salt

  /**
    * The user password
    */
  def password: String = _password

  /**
    * The customers of the user
    */
  def customers : List[Customer] = User getCustomers(id)

  /**
    * The projects of the user
    */
  def projects : List[Project] = User getProjects(id)

  /**
    * The rates
    */
  def rates : List[Rate] = User getRates(id)

  /**
    * Adds a customer with the given name and description to this user.
    * 
    * @param name - The name of the customer
    * @param description - Maybe the description of the customer
    * @return Maybe a customer
    */
  def addCustomer(name: String, description: Option[String]) : Option[Customer] = {
    Customer create(this, name, description)
  }

  /**
    * Adds a project with to the given customer for this usser with the given name and description if the customer exists
    * 
    * @param customer - The id of the customer
    * @param name - The name of the project
    * @param description - Maybe the description of the project
    * @return Maybe a project
    */
  def addProject(customer: Int, name: String, description: Option[String]) : Option[Project] = {
    Customer fromId(customer) match {
      case Some(c) => Project create(this, c, name, description)
      case None => None 
    } 
  }

  /**
    * Adds a registration to the given project with the given rate.
    * 
    * @param count - The number of time units
    * @param rate - The id of the rate
    * @param project - The id of the project
    * @return Maybe a registration
    */
  def addRegistration(count: Int, rate: Int, project: Int) {
    (Project fromId(project), Rate fromId(rate)) match {
      case (Some(p), Some(r)) => p addRegistration (count, r, this)
      case onFailure => None
    }
  }

  /**
    * Adds a rate to the user with the given name and rate.
    * 
    * @param name - The name of the rate
    * @param rate - The rate of the rate
    * @return Maybe a rate
    */
  def addRate(name: String, rate: Int) {
    Rate create(this, name, rate)
  }

  /**
    * Removes a customer with the given id.
    * 
    * @param id - The id of the customer.
    */
  def removeCustomer(id: Int) = {
    Customer delete(id, this)
  }

  /**
    * Removes a project with the given id.
    * 
    * @param id - The id of the project.
    */
  def removeProject(id: Int) = {
    Project delete(id, this)
  }

  /**
    * Removes a rate with the given id.
    * 
    * @param id - The id of the rate.
    */
  def removeRate(id: Int) = {
    Rate delete(id, this)
  }

  /**
    * Removes a registration with the given id.
    * 
    * @param id - The id of the registration.
    */
  def removeRegistration(id: Int) = {
    Registration delete(id, this)
  }

  override def toString(): String = {
    _name
  }
}
