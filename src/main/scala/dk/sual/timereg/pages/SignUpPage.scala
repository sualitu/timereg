package dk.sual.timereg.pages

import dk.sual.timereg.model.User
import dk.sual.timereg.pages.request.NoRequest
import dk.sual.timereg.Helpers.Pages.baseUrl

object SignUpPage extends Page {

  type RequestType = NoRequest

  protected def content(request: NoRequest, user: Option[User]) : String = {
    s"""
    <div class="container">

      <form name="signup" action="${baseUrl}signup" method="POST" class="form-signup">
        <h2 class="form-signin-heading">Please sign up</h2>
        <label for="name" class="sr-only">Name</label>
        <input name="name" type="name" id="name" class="form-control" placeholder="Name" required autofocus>
        <label for="password" class="sr-only">Password</label>
        <input name="password" type="password" id="password" class="form-control" placeholder="Password" required>
        <button class="btn btn-lg btn-primary btn-block" type="submit">Sign up</button>
      </form>

    </div> <!-- /container -->
    """
  }
}
