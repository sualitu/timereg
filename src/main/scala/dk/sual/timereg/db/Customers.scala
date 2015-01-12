package dk.sual.timereg.db

protected trait Customers extends Users {
  import scala.slick.driver.MySQLDriver.simple._
  import dk.sual.timereg.model.Customer

  protected class Customers(tag: Tag) extends TableWithId[Customer](tag, "CUSTOMERS") {
    def name        = column[String]("name"       , O.NotNull)
    def description = column[String]("description"           )
    def ownerId     = column[Int]   ("ownerId"    , O.NotNull)

    def owner = foreignKey ("fk_customer_owner_user", ownerId, users) (_.id, onDelete = ForeignKeyAction.Cascade)

    def * = (id, ownerId, name, description.?) <> ((Customer.apply _).tupled, Customer unapply)
  }

  protected val customers = TableQuery[Customers]
}
