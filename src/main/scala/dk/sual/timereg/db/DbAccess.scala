package dk.sual.timereg.db

trait DbAccess
  extends Users 
  with Projects
  with Customers 
  with Registrations
  with Rates 
  with Sessions {
  import dk.sual.timereg.Settings
  import dk.sual.timereg.Helpers.Util._
  import scala.slick.driver.MySQLDriver.simple._
  import scala.slick.jdbc.{ GetResult, StaticQuery => Q }

  protected val Db = Database.forURL(Settings.db.dbString, driver = Settings.db.dbDriver)

  /**
    * Resets the database
    */
  protected def resetDb = {
    println("Resetting DB")
    Db withSession { implicit session =>
      val createDatabase = Q.u + "CREATE DATABASE IF NOT EXISTS timereg"
      createDatabase.execute

      val dropTables = ddl.dropStatements

      for(d <- dropTables) {
        try {
          val drop = Q.u + d
          drop.execute
        } catch {
          case e : Throwable => log("Drop table failed.")
        }
      }
      ddl create
    }
  }

  protected val ddl = users.ddl ++ customers.ddl ++ projects.ddl ++ registrations.ddl ++ rates.ddl ++ sessions.ddl
}
