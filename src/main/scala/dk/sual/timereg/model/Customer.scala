package dk.sual.timereg.model


import dk.sual.timereg.Helpers.Util._
import dk.sual.timereg.db._

import scala.slick.driver.MySQLDriver.simple._

object Customer extends DbAccess {

  /**
    * Get the customer with the given id
    * 
    * @param id - the id of the customer
    * @return Maybe a customer
    */
  def fromId(id: Int) : Option[Customer] = Db withSession { implicit session =>
    val cs = for {
      c <- customers if c.id === id
    } yield c
    uniqueMatch(cs run)
  }

  /**
    * Creates a customer.
    * 
    * @param owner - The id of the owner(a user)
    * @param name - The name of the customer
    * @param description - A description of the customer
    * @return Maybe a customer
    */
  def create(owner: User, name: String, description: Option[String]) : Option[Customer] = Db withSession { implicit session =>
    val customer = Customer withoutId(owner.id, name, description)
    val id = (customers returning customers.map(_.id)) += customer
    fromId(id)
  }

  /**
    * Deletes a customer.
    * 
    * @param id - the id of the customer
    * @param user - the deletion invoker
    */
  def delete(id: Int, user: User) : Unit = Db withSession { implicit session =>
    val cs = for {
      c <- customers if (c.id === id) && (c.ownerId === user.id)
    } yield c
    cs.delete
    ()
  }

  /**
    * Creates a customer with an invalid id. This is used for adding customers to the database before their real id is known.
    * 
    * @param owner - The id of the owner(a user)
    * @param name - The name of the customer
    * @param description - A description of the customer
    * @return A customer
    */
  private def withoutId(owner: Int, name: String, description: Option[String]) = {
    Customer(-1, owner, name, description)
  }
}

case class Customer(
  val id: Int,
  private val _ownerId: Int,
  private var _name: String,
  private var _description: Option[String]) {

  /**
    * The owner of this customer.
    */
  def owner : Option[User] = User fromId(_ownerId)

  /**
    * The id of the owner
    */
  def ownerId = _ownerId

  /**
    * The projects of this customer.
    */
  def projects : List[Project] = Project ofCustomer(this)

  /**
    * The name of this customer
    */
  def name : String = _name

  /**
    * The description of this customer
    */
  def description : Option[String] = _description

  override def toString() : String = {
    _name
  }

}
