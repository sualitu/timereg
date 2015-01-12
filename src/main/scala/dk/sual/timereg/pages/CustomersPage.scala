package dk.sual.timereg.pages

import dk.sual.timereg.model.User
import dk.sual.timereg.model.Customer
import dk.sual.timereg.pages.Tables._
import dk.sual.timereg.pages.request.NoRequest
import dk.sual.timereg.Helpers.Pages.baseUrl

/**
  * A page presenting many customers
  */
object CustomersPage extends NavPage with CustomerTable {
  override val renderTitle = "All Customers"

  override type RequestType = NoRequest

  protected def tableContent(user: User) : List[Customer] = user customers

  protected def content(request: NoRequest, user: Option[User]) : String = inContainer(body(user))

  protected def body(user: Option[User]) : String = {
    val s = new StringBuilder()
    user match {
      case Some(u) => {
        s.append(inContainer(creation))
        s.append(renderTable(tableContent(u)))
        }
      case None => s.append(pleaseLogin)
    }
    s.toString()
  }

  protected def creation : String = {
s"""
<p>New customer:</p>
<form name="create" action="${baseUrl}customer/create" method="POST" class="form-inline"><input name="name" type="name" id="name" class="form-control" placeholder="Name" required> <input name="description" type="description" id="description" class="form-control" placeholder="Description" required> <button class="btn btn-default" type="submit">Create</button></form>
"""    
  }
}
