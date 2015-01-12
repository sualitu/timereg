package dk.sual.timereg

import org.specs2.mutable.Specification
import spray.testkit.Specs2RouteTest
import spray.http._
import StatusCodes._
import java.net.URLEncoder

import dk.sual.timereg.model._

class UserTests extends Specification with TimeregTests {

  "The user model class" should {
    "create a user" in {
      val name = randomUsername
      val pass = randomPassword

      User create(name, pass)

      (User fromName(name) isDefined) must beTrue
    }

    "give a user from an id if the user exists" in {
      val opt = createRandomUser
      (opt isDefined) must beTrue
      val user = opt get

      val otherOpt = User fromId(user.id)
      (otherOpt isDefined) must beTrue
      val otherUser = otherOpt get

      (user.name == otherUser.name) must beTrue
      (user.password == otherUser.password) must beTrue
      (user.id == otherUser.id) must beTrue
    }

    "not give a user from an id if the user does not exist" in {
      (User fromId(-1) isDefined) must beFalse
    }

    "give a project for a user with projects" in {
      val uOpt = createRandomUser
      (uOpt isDefined) must beTrue
      val user = uOpt get
      val cOpt = createRandomCustomer(user)
      (cOpt isDefined) must beTrue
      val customer = cOpt get
      val pOpt = createRandomProject(user, customer)
      (pOpt isDefined) must beTrue
      val project = pOpt get

      (user.projects contains project) must beTrue
    }
  }
}
