package dk.sual.timereg.db

protected trait Users {
  import scala.slick.driver.MySQLDriver.simple._

  import dk.sual.timereg.model.User

  protected class Users(tag: Tag) extends TableWithId[User](tag, "USERS") {
    def name     = column[String]("name"    ,                          O.NotNull)
    def password = column[String]("password",                          O.NotNull)
    def salt     = column[String]("salt"    ,                          O.NotNull)

    def * = (id, name, password, salt) <> ((User.apply _).tupled, User unapply)
  }

  protected val users = TableQuery[Users]
}
