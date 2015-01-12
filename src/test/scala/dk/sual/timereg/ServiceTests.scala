package dk.sual.timereg

import org.specs2.mutable.Specification
import spray.testkit.Specs2RouteTest
import spray.http._
import StatusCodes._
import java.net.URLEncoder

import dk.sual.timereg.model._

class ServiceTests extends Specification with Specs2RouteTest with Services with TimeregTests {
  def actorRefFactory = system

  "The time registration services" should {

    "return redirect to /signup for GET request to the root path" in {
      Get() ~> route ~> check {
        responseAs[String] must contain("/signup")
      }
    }

    "return OK for a GET request to signup" in {
      Get("/signup") ~> route ~> check {
        status === StatusCodes.OK
      }
    }

    "return OK for a GET request to signin" in {
      Get("/signin") ~> route ~> check {
        status === StatusCodes.OK
      }
    }

    "log a user in with the appropicate details" in {
      val pass = randomPassword
      val optU = createRandomUserWithPass(pass)
      (optU isDefined) must beTrue
      val user = optU get

      val body = entityBody(List(
        ("name", user.name),
        ("password", pass))
      )
      val entity = entityWithBody(body)
    
      val post = Post("/signin", entity)

      post ~> route ~> check {
        (Session fromUser(user.id) isDefined) must beTrue
        status === Found
      }
    }

    "sign a user up" in {
      val name = randomUsername
      val pass = randomPassword

      val body = entityBody(List(
        ("name", name),
        ("password", pass))
      )
      val entity = entityWithBody(body)
    
      val post = Post("/signup", entity)
      post ~> route ~> check {
        (User fromName(name) isDefined) must beTrue
        status === SeeOther
      }
    }

    "if user is logged in Get on customers should be OK" in {
      val opt = randomLoggedInUser
      (opt isDefined) must beTrue
      val (_, session) = opt get
      val header = cookieHeader(session.key.toString())

      val get = Get("/customers") ~> header

      get ~> route ~> check {
        status === OK
      }
    }

    "if user is logged in Get on projects should be OK" in {
      val opt = randomLoggedInUser
      (opt isDefined) must beTrue
      val (_, session) = opt get
      val header = cookieHeader(session.key.toString())

      val get = Get("/projects") ~> header

      get ~> route ~> check {
        status === OK
      }
    }

    "if user is logged in Get on rates should be OK" in {
      val opt = randomLoggedInUser
      (opt isDefined) must beTrue
      val (_, session) = opt get
      val header = cookieHeader(session.key.toString())

      val get = Get("/rates") ~> header

      get ~> route ~> check {
        status === OK
      }
    }  

    "if a user logs out the session is no longer valid" in {
      val opt = randomLoggedInUser
      (opt isDefined) must beTrue
      val (user, session) = opt get
      val header = cookieHeader(session.key.toString())

      val get = Get("/signout") ~> header

      get ~> route ~> check {
        (Session fromUser(user.id) isDefined) must beFalse
        status === SeeOther
      }
    }

    "reject unauthorized access to /customers" in {
      Get("/customers") ~> route ~> check {
        status === Unauthorized
      }
    }
    "reject unauthorized access to /projects" in {
      Get("/projects") ~> route ~> check {
        status === Unauthorized
      }
    }
    "reject unauthorized access to /rates" in {
      Get("/rates") ~> route ~> check {
        status === Unauthorized
      }
    }
  }
}
