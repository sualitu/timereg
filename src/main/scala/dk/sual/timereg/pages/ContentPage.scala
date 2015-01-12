package dk.sual.timereg.pages

import dk.sual.timereg.model.User
import dk.sual.timereg.model.Customer
import dk.sual.timereg.pages.request.CustomerPageRequest

/**
  * A page for content. 
  * 
  * @param A - the type of content 
  */
trait ContentPage[A] extends NavPage {

  /**
    * The labels of the content
    */
  protected def left(resource : A, user: Option[User]) : String

  /**
    * The content
    */
  protected def right(resource : A, user: Option[User]) : String

  protected def found(resource : A, user: Option[User]) : String = {
s"""
<div class="row-fluid">
  <div class="col-lg-4">
${left(resource, user)}
  </div>
  <div class="col-lg-8">    
${right(resource, user)}
</div>
</div>
"""
  }

  /**
    * What to do if the sought after data could not be found.
    */
  protected def notFound : String = {
    "<p>Content not found</p>"
  }
}
