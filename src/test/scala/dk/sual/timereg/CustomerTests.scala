package dk.sual.timereg

import org.specs2.mutable.Specification
import spray.testkit.Specs2RouteTest
import spray.http._
import StatusCodes._
import java.net.URLEncoder

import dk.sual.timereg.model._

class CustomerTests extends Specification with TimeregTests {

  "The customer model class" should {
    "only delete customers when the invoker is the owner" in {
      val uOpt1 = createRandomUser
      (uOpt1 isDefined) must beTrue
      val user1 = uOpt1 get

      val uOpt2 = createRandomUser
      (uOpt2 isDefined) must beTrue
      val user2 = uOpt2 get

      val cOpt = createRandomCustomer(user1)
      (cOpt isDefined) must beTrue
      val customer = cOpt get

      (Customer fromId(customer.id) isDefined) must beTrue
      user2 removeCustomer(customer id)
      (Customer fromId(customer.id) isDefined) must beTrue
      user1 removeCustomer(customer id)
      (Customer fromId(customer.id) isDefined) must beFalse
    }
  }
}
