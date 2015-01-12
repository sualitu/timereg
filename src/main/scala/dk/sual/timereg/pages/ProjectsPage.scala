package dk.sual.timereg.pages

import dk.sual.timereg.model.User
import dk.sual.timereg.model.Project
import dk.sual.timereg.model.Customer
import dk.sual.timereg.pages.request.NoRequest
import dk.sual.timereg.pages.Tables.ProjectTable
import dk.sual.timereg.Helpers.Pages._

object ProjectsPage extends NavPage with ProjectTable {
  override val renderTitle = "All Projects"

  override type RequestType = NoRequest

  protected def tableContent(user: User) : List[Project] = user projects

  protected def content(request: NoRequest, user: Option[User]) : String = inContainer(body(user))

  protected def body(user: Option[User]) : String = {
    val s = new StringBuilder()
    user match {
      case Some(u) => {
        s.append(inContainer(creation(u)))
        s.append(renderTable(tableContent(u)))
      }
      case None => s.append(pleaseLogin)
    }
    s.toString()
  }

  protected def creation(u : User) : String = {
s"""
<p>New project: </p>
<form name="create" action="${baseUrl}project/create" method="POST" class="form-inline"><input name="name" type="name" id="name" class="form-control" placeholder="Name" required> <input name="description" type="description" id="description" class="form-control" placeholder="Description" required> ${customerDropDown(u.customers)} <input type="hidden" name="ret" value="/projects"> <button class="btn btn-default" type="submit">Create</button></form>
"""    
  }

  protected def customerDropDown(cs : List[Customer]) : String = {
    val s = new StringBuilder()
    s.append("""<select class="form-control" id="customer" name="customer">""")
    cs.foldLeft(s) { (sb, c) => sb append option(c) }
    s.append("""</select>""")
    s.toString()
  }

  protected def option(customer : Customer) : String = {
    s"""
    <option value="${customer.id}">${customer.name}</option>
    """
  }
}
