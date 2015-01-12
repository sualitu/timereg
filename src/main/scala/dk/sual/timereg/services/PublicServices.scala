package dk.sual.timereg.services

import spray.routing._
import spray.http._
import MediaTypes._
import java.util.UUID

import dk.sual.timereg.model._
import dk.sual.timereg.pages._
import dk.sual.timereg.service.TimeregService
import dk.sual.timereg.pages.request._
import dk.sual.timereg.Helpers.StringUtils._

trait PublicServices extends TimeregService {

  val publicRoute =
    pathPrefix("css") {
      get {
        getFromResourceDirectory("assets/css")
      }
    } ~
    pathPrefix("js") {
      get {
        getFromResourceDirectory("assets/js")
      }
    } ~
    path("signup") {
      post {
        formFields('name, 'password) { (name, password) =>
          User create(name, password) match {
            case Some(_) => redirectSignin
            case None => redirectSignup
          }
        }
      }
    } ~
    path("signup") {
      get {
        val success = {  _ : Session => redirect("/customers", StatusCodes.Found) }
        withOptionalCookie(success, html(SignUpPage(None, NoRequest())))
      }
    } ~
    path("signin") {
      post {
        formFields('name, 'password) { (name, password) =>
          User login(name, password) match {
            case Some((_,s)) => setCookie(s.key.toString(), redirect("/customers", StatusCodes.Found))
            case None => redirectSignup
          }
        }
      }
    } ~
    path("signin") {
      get {
        val success = {  _ : Session => redirect("/customers", StatusCodes.Found) }
        withOptionalCookie(success, html(SignInPage(None, NoRequest())))
      }
    }
}
