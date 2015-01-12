package dk.sual.timereg.model

import dk.sual.timereg.db.DbAccess
import dk.sual.timereg.Helpers.Util._

import scala.slick.driver.MySQLDriver.simple._

object Registration extends DbAccess {

  /**
    * Gets a registration with the given id
    * 
    * @param id - The id of the regitration
    * @return Maybe a registration
    */
  def fromId(id: Int) : Option[Registration] = Db withSession { implicit session =>
    val rs = for {
      r <- registrations if r.id === id
    } yield r
    uniqueMatch(rs run)
  }

  /**
    * Gets the registrations of a project.
    * 
    * @param id - the id of the project
    * @return A list of registrations
    */
  def fromProject(id: Int) : List[Registration] = Db withSession { implicit session =>
    val rs = for {
      r <- registrations if r.projectId === id
    } yield r
    rs list
  }

  /**
    * Creates a registration.
    * 
    * @param count - the number of time units
    * @param projectId - the project
    * @param rateId - the rate
    * @return Maybe a registration
    */
  def create(count: Int, project: Project, rate: Rate, invoker: User) : Option[Registration] = Db withSession { implicit session =>
    val registration = withoutId(count, project.id, rate.id)
    if(invoker.id == project.ownerId) {
      val id = (registrations returning registrations.map(_.id)) += registration
      fromId(id)
    } else {
      None
    }
  }

  /**
    * Deletes a registration with the given id
    * 
    * @param id - the id of the registration
    * @param user - the deletion invoker
    */
  def delete(id: Int, user: User) : Unit = Db withSession { implicit session =>
    val projects = user.projects map (_.id)
    val rs = for {
      r <- registrations if (r.id === id) && (projects contains (r.projectId))
    } yield r
    rs.delete
    ()
  }

  /**
    * Creates a rate with an invalid id. This is used for adding rates to the database before their real id is known.
    * 
    * @param count - the number of time units
    * @param projectId - the project
    * @param rateId - the rate
    * @return A registration
    */
  private def withoutId(count: Int, projectId: Int, rateId: Int) : Registration = Registration (-1, count, projectId, rateId)
}

case class Registration(
  val id: Int,
  private val _count: Int,
  private val _projectId: Int,
  private val _rateId: Int) {

  import dk.sual.timereg.model.Project
  import dk.sual.timereg.model.Rate

  /**
    * The total value of this registration (count times rate)
    * 
    * @return Maybe an integer
    */
  def amount : Option[Int] = {
    Rate fromId(_rateId) match {
      case Some(r) => Some(_count * r.rate)
      case None => None
    }
  }

  /**
    * The registration count
    */
  def count : Int = _count

  /**
    * The registration project
    */
  def project : Option[Project] = Project fromId(_projectId)

  /**
    * The registration rate
    */
  def rate : Option[Rate] = Rate fromId(_rateId)

  override def toString() : String = {
    s"$project($amount)"
  }
}
