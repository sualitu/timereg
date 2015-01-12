package dk.sual.timereg

import scala.slick.driver.MySQLDriver.simple._

object Helpers {
  object Pages {
    /**
      * The baseurl of this application
      */
    val baseUrl = dk.sual.timereg.Settings.pages.baseUrl
  }
  object StringUtils {
    /**
      * Converts a string to an integer safely
      * 
      * @param s - the string
      * @return Maybe an integer
      */
    implicit class StringImprovements(val s: String) {
      import scala.util.control.Exception._
      def toIntOpt = catching(classOf[NumberFormatException]) opt s.toInt
    }
  }
  object Util {
    def log(s : String) {
      println(s"Log: $s")
    }

    /**
      * Takes a sequence. If it is a singleton list it returns the element. Else None.
      * 
      * @param input - the sequence
      * @return Maybe an A
      */
    def uniqueMatch[A](input: Seq[A]): Option[A] = { 
      input match {
        case i +: Nil => Some(i)
        case onFailure => None
      }
    }
  }
}
