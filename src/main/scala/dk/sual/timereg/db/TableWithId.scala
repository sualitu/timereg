package dk.sual.timereg.db

import scala.slick.driver.MySQLDriver.simple._

abstract class TableWithId[A](tag: Tag, tableName: String) extends Table[A](tag, tableName) {
  def id = column[Int]("id", O.PrimaryKey, O.AutoInc, O.NotNull)
}
