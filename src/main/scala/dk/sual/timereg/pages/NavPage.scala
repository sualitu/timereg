package dk.sual.timereg.pages

import dk.sual.timereg.model.User
import dk.sual.timereg.Helpers.Pages._

/**
  * A page with a navigation bar.
  */
trait NavPage extends Page {
  /**
    * Adds the navigation bar to the standard header
    */
  override def header : String = {
    val s = new StringBuilder()
    s.append(super.header).append(nav).append(inContainer(s"<h3>$renderTitle</h3>")).toString()
  }

  protected val renderTitle : String

  /**
    * The navigation bar
    */
  protected def nav : String = {
    s"""
    <nav class="navbar navbar-default navbar-static-top">
      <div class="container">
        <div class="navbar-header">
          <p class="navbar-brand">Time Registration</p>
        </div>
        <div id="navbar" class="navbar-collapse collapse">
          <ul class="nav navbar-nav">
            <li><a href="${baseUrl}projects">Projects</a></li>
            <li><a href="${baseUrl}customers">Customers</a></li>
            <li><a href="${baseUrl}rates">Rates</a></li>
          </ul>
          <ul class="nav navbar-nav navbar-right">
$logged
          </ul>
        </div>
      </div>
    </nav>
"""
  }

  /**
    * What to do when the user was not logged in
    */
  protected def pleaseLogin : String = {
s"""
<p>
You are not logged in.<br>
<a href="${baseUrl}signin">Click here to log in!</a>
</p>
"""
  }

  /**
    * "Logged in as" label and Logout button
    */
  protected def logged : String = {
    user match {
      case Some(u) => {
s"""
            <li><a>Logged in as ${u.name}</a></li>        
            <li><a href="${baseUrl}signout">Logout</a></li>        
"""
      }
      case None => {
       s"""
            <li>$pleaseLogin</li>
        """
      }
    }
  }
}
