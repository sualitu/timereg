package dk.sual.timereg.db

protected trait Registrations 
    extends Projects 
    with Rates {
  import scala.slick.driver.MySQLDriver.simple._
  import dk.sual.timereg.model.Registration
  import dk.sual.timereg.model.Project
  import dk.sual.timereg.model.Rate

  protected class Registrations(tag: Tag) extends TableWithId[Registration](tag, "REGISTRATIONS") {
    def count     = column[Int]("count"    , O.NotNull)
    def projectId = column[Int]("projectId", O.NotNull)
    def rateId    = column[Int]("rateId"   , O.NotNull)

    def project = foreignKey ("fk_registration_project", projectId, projects) (_.id, onDelete = ForeignKeyAction.Cascade)
    def rate = foreignKey ("fk_registration_rate", rateId, rates) (_.id, onDelete = ForeignKeyAction.Cascade)

    def * = (id, count, projectId, rateId) <> ((Registration.apply _).tupled, Registration unapply)
  }

  protected val registrations = TableQuery[Registrations]
}
