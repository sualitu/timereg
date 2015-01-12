package dk.sual.timereg.pages

import dk.sual.timereg.model.User
import dk.sual.timereg.model.Rate
import dk.sual.timereg.pages.Tables._
import dk.sual.timereg.pages.request.NoRequest
import dk.sual.timereg.Helpers.Pages.baseUrl

object RatesPage extends NavPage with RateTable {
  override val renderTitle = "All Rates"

  override type RequestType = NoRequest

  protected def tableContent(user: User) : List[Rate] = user rates

  protected def content(request: NoRequest, user: Option[User]) : String = inContainer(body(user))

  protected def body(user: Option[User]) : String = {
    val s = new StringBuilder()
    user match {
      case Some(u) => {
        s.append(creation)
        s.append(renderTable(tableContent(u)))
      }
      case None => s.append(pleaseLogin)
    }
    s.toString()
  }

  protected def creation : String = {
s"""
<p>New rate: </p>
<form name="create" action="${baseUrl}rate/create" method="POST" class="form-inline"><input name="name" type="name" id="name" class="form-control" placeholder="Name" required> <input name="rate" type="rate" id="rate" class="form-control" placeholder="Rate" required> <button class="btn btn-default" type="submit">Create</button></form>
"""    
  }
}
