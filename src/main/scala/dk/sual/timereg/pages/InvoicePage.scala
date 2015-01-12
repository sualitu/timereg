package dk.sual.timereg.pages

import dk.sual.timereg.model.User
import dk.sual.timereg.model.Project
import dk.sual.timereg.model.Rate
import dk.sual.timereg.pages.request.ProjectPageRequest
import dk.sual.timereg.pages.Tables.InvoiceTable
import dk.sual.timereg.Helpers.Pages._

/**
  * Webpage for showing invoices.
  */
object InvoicePage extends Page with InvoiceTable {
  type RequestType = ProjectPageRequest

  protected def content (request: ProjectPageRequest, user: Option[User]) : String = {
    request.project match {
      case Some(p) => {
        val s = new StringBuilder()
        val top = inContainer(invoiceHeader(p, user))
        val table = inContainer(renderTable(p registrations))
        s.append(top).append(table).toString()
      }
      case None => "Something went wrong!"
    }
  }

  protected def invoiceHeader(project: Project, user: Option[User]) : String = {
    s"""
<div class="row-fluid">
<h1>${user getOrElse "Nothing"}</h1>
  <div class="col-lg-4">
<p class="text-left">Customer:<p>
<p class="text-left">Project:</p>
<p class="text-left">Description:</p>
  </div>
  <div class="col-lg-8">    
<p class="text-right">${project.customer getOrElse ""}</p>
<p class="text-right">${project}</p>
<p class="text-right">${project.description getOrElse ""}</p>
</div>
</div>
     """
  }

}
