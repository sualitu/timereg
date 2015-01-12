package dk.sual.timereg.db

protected trait Sessions extends Users {
  import scala.slick.driver.MySQLDriver.simple._
  import dk.sual.timereg.model.Session
  
  protected class Sessions(tag: Tag) extends Table[Session](tag, "SESSIONS") {
    def key    = column[String]("session_key", O.PrimaryKey, O.NotNull)
    def userId = column[Int]   ("user"       ,               O.NotNull)

    def user = foreignKey ("fk_session_user", userId, users) (_.id, onDelete = ForeignKeyAction.Cascade)

    def * = (key, userId) <> ((Session.apply _).tupled, Session unapply)
  }

  protected val sessions = TableQuery[Sessions]
}
