package dk.sual.timereg.pages

import dk.sual.timereg.model._
import dk.sual.timereg.pages.request._
import dk.sual.timereg.Helpers.Pages._

trait Page {

  type RequestType <: Request

  var user : Option[User] = None

  /**
    * The content of the page
    */
  protected def content(request: RequestType, user: Option[User]) : String

  /**
    * The footer
    */
  protected def footer : String = {
    """
      </body>
    </html>
    """
  }

  /**
    * The header
    */
  protected def header : String = {
   s"""
<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="">
    <meta name="author" content="">

    <title>Timereg</title>

    <link href="${baseUrl}css/bootstrap.min.css" rel="stylesheet">
    <script src="${baseUrl}js/jquery-1.11.2.min.js"></script>
    <script src="${baseUrl}js/bootstrap.min.js"></script>

  </head>

  <body>
       
    """
  }

  /** 
    * Creation
    * 
    * @param session - Maybe the session of this page
    * @param request - the request
    * @return a string
    */
  def apply(session: Option[Session], request: RequestType) : String = {
    render(session, request)
  }

  /**
    * Wraps the input in a container
    * 
    * @param input - the input
    * @return a string
    */
  def inContainer(input: String) : String = {
s"""
        <div class="container">
$input
        </div>
"""
  }

  /**
    * Renders a page based on a session an a request
    * 
    * @param session - Maybe the session of this page
    * @param request - The request
    * @return a string
    */
  def render(session: Option[Session], request: RequestType) : String = {
    val page = new StringBuilder()
    val u = session match {
      case Some(s) => s.user
      case None => None
    }
    user = u
    page.append(header).append(content(request, u)).append(footer).toString()
  }
}
