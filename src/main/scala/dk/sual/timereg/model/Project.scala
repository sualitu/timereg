package dk.sual.timereg.model

import dk.sual.timereg.db.DbAccess
import dk.sual.timereg.Helpers.Util._

import scala.slick.driver.MySQLDriver.simple._

object Project extends DbAccess {
  /**
    * Gets the project with the given id.
    * 
    * @param id - the id of the project.
    * @return Maybe a project
    */
  def fromId(id: Int) : Option[Project] = Db withSession { implicit session =>
    val ps = for {
      p <- projects if p.id === id
    } yield p
    uniqueMatch(ps run)
  }

  /**
    * Gets the projects for a given customer.
    * 
    * @param customer - The customer
    * @return A list of projects.
    */
  def ofCustomer(customer : Customer) : List[Project] = Db withSession { implicit session =>
    val ps = for {
      p <- projects if p.customerId === customer.id
    } yield p

    ps.list
  }

  /**
    * Creates a project with the given owner, customer, name and description.
    * 
    * @param owner - the id of the owner
    * @param customer - the id of the customer
    * @param name - the name
    * @param description - the description
    * @return Maybe a project
    */
  def create(owner: User, customer: Customer, name: String, description: Option[String]) : Option[Project] = Db withSession { implicit session =>
    val project = Project withoutId(owner.id, customer.id, name, description)
    if(owner.id == customer.ownerId) {
      val id = (projects returning projects.map(_.id)) += project
      fromId(id)
    } else {
      None
    }
  }

  /**
    * Deletes a project.
    * 
    * @param id - the id of the project
    * @param user - the deletion invoker
    */
  def delete(id: Int, user: User) : Unit = Db withSession { implicit session =>
    val ps = for {
      p <- projects if (p.id === id) && (p.ownerId === user.id)
    } yield p
    ps.delete
    ()
  }

  /**
    * Creates a project with an invalid id. This is used for adding projects to the database before their real id is known.
    * 
    * @param owner - the id of the owner
    * @param customer - the id of the customer
    * @param name - the name
    * @param description - the description
    * @return A project
    */
  private def withoutId(owner: Int, customer: Int, name: String, description: Option[String]) = {
    Project(-1, owner, customer, name, description)
  }
}

case class Project(
  val id: Int,
  private val _ownerId: Int,
  private var _customerId: Int,
  private var _name: String,
  private var _description: Option[String]) {

  /**
    * The project name
    */
  def name : String = _name

  /**
    * The project description
    */
  def description : Option[String] = _description

  /**
    * The project owner
    */
  def owner : Option[User] = User fromId(_ownerId)

  /**
    * The owner id
    */
  def ownerId = _ownerId

  /**
    * The project customer
    */
  def customer : Option[Customer] = Customer fromId(_customerId)

  /**
    * All registration associated with this project.
    */
  def registrations : List[Registration] = Registration fromProject(id)

  /**
    * Adds a registration to the project.
    * 
    * @param count - the number of time units
    * @param rate - the id of the rate
    * @return Maybe a registration
    */
  def addRegistration(count: Int, rate: Rate, invoker: User) : Option[Registration] = {
    Registration create(count, this, rate, invoker)
  }

  override def toString() : String = {
    _name
  }
}
