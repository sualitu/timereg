package dk.sual.timereg.pages

import dk.sual.timereg.model.User
import dk.sual.timereg.model.Customer
import dk.sual.timereg.model.Project
import dk.sual.timereg.pages.request.CustomerPageRequest
import dk.sual.timereg.pages.Tables.ProjectTable
import dk.sual.timereg.Helpers.Pages._

/**
  * A page presenting a customer
  */
object CustomerPage extends ContentPage[Customer] with ProjectTable {
  override val renderTitle = "Customer"

  override type RequestType = CustomerPageRequest

  protected override def left(resource : Customer, user: Option[User]) : String = {
"""
    <p class="text-left">Name:</p>
    <p class="text-left">Description:</p>
"""
  }

  protected override def right(resource : Customer, user: Option[User]) : String = {
s"""
    <p class="text-left">${resource.name}</p>
    <p class="text-left">${resource.description getOrElse "Nothing"}</p>
"""
  }

  protected def content (request: CustomerPageRequest, user: Option[User]) : String = {
    request.customer match {
      case Some(c) => {
        val s = new StringBuilder()
        val top = inContainer(found(c, user))
        val cre = creation(c.id)
        val table = inContainer(renderTable(c projects))
        inContainer(s.append(top).append(cre).append(table).toString())
      }
      case None => notFound
    }
  }

  protected def creation(customerId: Int) : String = {
s"""
<p>New project: </p>
<form name="create" action="${baseUrl}project/create" method="POST" class="form-inline"><input name="name" type="name" id="name" class="form-control" placeholder="Name" required> <input name="description" type="name" id="description" class="form-control" placeholder="Description" required> <input name="customer" type="hidden" value=${customerId}> <input name="ret" type="hidden" value="/customer/${customerId}"> <button class="btn btn-default" type="submit">Create</button></form>
"""    
  }

}
