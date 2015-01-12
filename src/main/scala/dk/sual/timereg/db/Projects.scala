package dk.sual.timereg.db

protected trait Projects 
    extends Customers
    with    Users {
  import scala.slick.driver.MySQLDriver.simple._
  import dk.sual.timereg.model.Project

  protected class Projects(tag: Tag) extends TableWithId[Project](tag, "PROJECTS") {
    def name        = column[String]("name"       , O.NotNull)
    def description = column[String]("description"           )
    def ownerId     = column[Int]   ("ownerId"    , O.NotNull)
    def customerId  = column[Int]   ("customerId" , O.NotNull)

    def owner    = foreignKey ("fk_project_owner_user", ownerId   , users)     (_.id, onDelete = ForeignKeyAction.Cascade)
    def customer = foreignKey ("fk_project_customer"  , customerId, customers) (_.id, onDelete = ForeignKeyAction.Cascade)

    def * = (id, ownerId, customerId, name, description.?) <> ((Project.apply _).tupled, Project unapply)
  }

  protected val projects = TableQuery[Projects]
}
