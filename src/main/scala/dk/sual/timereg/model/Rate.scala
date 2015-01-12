package dk.sual.timereg.model

import dk.sual.timereg.db.DbAccess
import dk.sual.timereg.Helpers.Util._

import scala.slick.driver.MySQLDriver.simple._

object Rate extends DbAccess {
  /**
    * Gets a rate with the given id
    * 
    * @param id - The id of the rate
    * @return Maybe a rate
    */
  def fromId(id: Int) : Option[Rate] = Db withSession { implicit session =>
    val rs = for {
      r <- rates if r.id === id
    } yield r
    uniqueMatch(rs run)
  }

  /**
    * Gets the rates of a user
    * 
    * @param id - the id of the user
    * @return A list of rates
    */
  def fromUserId(id: Int) : List[Rate] = Db withSession { implicit session => 
    val rs = for {
      r <- rates if r.id === id
    } yield r
    rs.list
  }

  /**
    * Creates a rate.
    * 
    * @param owner - the id of the owner
    * @param name - the name
    * @param rate - the rate
    * @return Maybe a rate
    */
  def create(owner: User, name: String, rate: Int) : Option[Rate] = Db withSession { implicit session =>
    val r = Rate withoutId(owner.id, name, rate)
    val id = (rates returning rates.map(_.id)) += r
    fromId(id)
  }

  /**
    * Creates a rate with an invalid id. This is used for adding rates to the database before their real id is known.
    * 
    * @param owner - the id of the owner
    * @param name - the name
    * @param rate - the rate
    */
  def withoutId(owner: Int, name: String, rate: Int) : Rate = {
    Rate(-1, owner, name, rate)
  }

  /**
    * Deletes a rate
    * 
    * @param id - the rate id
    * @param user - the invoker of the deletion
    */
  def delete(id: Int, user: User) : Unit = Db withSession { implicit session =>
    val rs = for {
      r <- rates if (r.id === id) && (r.userId === user.id)
    } yield r
    rs.delete
    ()
  }
}

case class Rate(
  val id: Int,
  private val _ownerId: Int,
  private val _name: String,
  private val _rate: Int) {

  /**
    * The rate name
    */
  def name = _name

  /**
    * The owner id
    */
  def ownerId = _ownerId

  /**
    * The rate
    */
  def rate = _rate

  override def toString() : String = {
    _name
  }
}
