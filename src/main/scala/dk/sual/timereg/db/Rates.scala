package dk.sual.timereg.db

protected trait Rates extends Users {
  import scala.slick.driver.MySQLDriver.simple._
  import dk.sual.timereg.model.Rate

  protected class Rates(tag: Tag) extends TableWithId[Rate](tag, "RATES") {   
    def userId = column[Int]    ("userId", O.NotNull)
    def name   = column[String] ("name"  , O.NotNull)
    def rate   = column[Int]    ("rate"  , O.NotNull)

    def * = (id, userId, name, rate) <> ((Rate.apply _).tupled, Rate unapply)

    def user = foreignKey ("fk_rate_user", userId, users) (_.id, onDelete = ForeignKeyAction.Cascade)
  }

  protected val rates = TableQuery[Rates]
}
