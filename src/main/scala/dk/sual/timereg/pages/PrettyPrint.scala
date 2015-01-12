package dk.sual.timereg.pages

import dk.sual.timereg.model._
import dk.sual.timereg.Helpers.Pages._

trait PrettyPrint {
  /**
    * Shows and wraps an option in a url if it was defined. Else ""
    */
  protected def showOptionCust : Option[Customer] => String = {
    case Some(i) => s"""<a href="${baseUrl}customer/${i.id}">$i</a>"""
    case None => "Nothing"
  }
  protected def showOptionUser : Option[User] => String = {
    case Some(i) => i.toString()
    case None => "Nothing"
  }
  protected def showOptionProj : Option[Project] => String = {
    case Some(i) => s"""<a href="${baseUrl}project/${i.id}">$i</a>"""
    case None => "Nothing"
  }
  protected def showOptionRate : Option[Rate] => String = {
    case Some(i) => i.toString()
    case None => "Nothing"
  }
  protected def showOptionReg : Option[Registration] => String = {
    case Some(i) => i.toString()
    case None => "Nothing"
  }
}
