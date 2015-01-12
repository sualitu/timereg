package dk.sual.timereg.service

import java.util.UUID
import spray.http._
import spray.routing._
import MediaTypes._
import spray.http.HttpHeaders._
import spray.routing.HttpService._

import dk.sual.timereg.model._

trait TimeregService extends HttpService {
  type ReqCon = RequestContext => Unit

  protected def sessionCookie = "timeregSession"

  def html(s : String) : ReqCon = { 
    respondWithMediaType(`text/html`) {
      complete(s)
    }
  }

  def withOptionalCookie(success: Session => ReqCon, fail: ReqCon) : ReqCon = {
    optionalCookie(sessionCookie) {
      case Some(c) =>
            Session fromKey(UUID fromString(c.content)) match {
              case Some(s) => success(s)
              case None => fail
            }
      case None => fail
    }
  }

  def withCookie(action : Session => ReqCon) : ReqCon = {
    withOptionalCookie(action, unauthorized)
  }

  def setCookie(c : String, after : ReqCon) : ReqCon = {
    setCookie(HttpCookie(sessionCookie, content = c)) {
       after
    }
  }

  def userAction(f : User => Unit) (ret: String) : Session => ReqCon = { s : Session =>
    User fromSession(s.key) match {
      case Some(u) => {
        f(u)
        redirect(ret, StatusCodes.Found)
      }
        case None => redirectSignup
     }
  }

  def returningTo(f : String => Session => ReqCon, s : String) : Session => ReqCon = f(s)

  def redirectSignup : StandardRoute = redirect("/signup", StatusCodes.SeeOther)

  def redirectSignin : StandardRoute = redirect("/signin", StatusCodes.SeeOther)

  def unauthorized : StandardRoute = complete{ HttpResponse(StatusCodes.Unauthorized, "You must be logged in") }
}
