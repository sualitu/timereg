package dk.sual.timereg.pages

import dk.sual.timereg.model.User
import dk.sual.timereg.model.Project
import dk.sual.timereg.model.Rate
import dk.sual.timereg.pages.request.ProjectPageRequest
import dk.sual.timereg.pages.Tables.RegistrationTable
import dk.sual.timereg.Helpers.Pages._

object ProjectPage extends ContentPage[Project] with RegistrationTable {
  override val renderTitle = "Project"

  type RequestType = ProjectPageRequest

  protected override def left(resource : Project, user: Option[User]) : String = {
"""
    <p class="text-left">Name:</p>
    <p class="text-left">Customer:</p>
    <p class="text-left">Description:</p>
"""
  }

  protected override def right(resource : Project, user: Option[User]) : String = {
s"""
    <p class="text-left">${resource.name}</p>
    <p class="text-left">${resource.customer getOrElse "Nothing"}</p>
    <p class="text-left">${resource.description getOrElse "Nothing"}</p>
"""
  }

  protected def content (request: ProjectPageRequest, user: Option[User]) : String = {
    request.project match {
      case Some(p) => {
        val s = new StringBuilder()
        val top = inContainer(found(p, user))
        val reg = inContainer(timeRegistration(user, p.id))
        val table = inContainer(renderTable(p registrations))
        val link = inContainer(invoiceLink(p))
        s.append(top).append(reg).append(link).append(table).toString()
      }
      case None => notFound
    }
  }

  protected def timeRegistration(u : Option[User], projectId : Int) : String = {
s"""
<p>Register time:</p><form name="create" action="${baseUrl}registration/create" method="POST" class="form-inline"><input name="count" type="count" id="count" class="form-control" placeholder="Count" required> <input name="project" type="hidden" value="$projectId"> ${rateDropDown(u)} <button class="btn btn-default" type="submit">Create</button></form>
"""        
  }

  protected def rateDropDown : Option[User] => String = {
    case Some(u) => rates(u.rates)
    case None => rates(Nil)
  }

  protected def rates(cs : List[Rate]) : String = {
    val s = new StringBuilder()
    s.append("""<select class="form-control" id="rate" name="rate">""")
    cs foreach { x => s.append(option(x)) }
    s.append("""</select>""")
    s.toString()
  }

  protected def option(rate : Rate) : String = {
    s"""
      <option value="${rate.id}">${rate.name}</option>
    """
  }  

  protected def invoiceLink(project : Project) : String = {
    s"""
     <a href="${baseUrl}invoice/${project.id}">Create invoice!</a>
    """
  }
}
