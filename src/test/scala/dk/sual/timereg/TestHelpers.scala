package dk.sual.timereg

import dk.sual.timereg.model._

import org.specs2.mutable.Specification
import spray.testkit.Specs2RouteTest
import spray.http._
import StatusCodes._
import scala.util.Random
import java.net.URLEncoder
import HttpHeaders._
import spray.httpx.RequestBuilding._

trait TimeregTests {
  private val rnd = new Random()

  def randomPassword = rnd.nextString(10)
  def randomUsername = rnd.nextString(10)
  def randomCustname = rnd.nextString(10)
  def randomProjname = rnd.nextString(10)
  def randomRatename = rnd.nextString(10)
  def randomDescript = Some(rnd.nextString(10))
  def randomRate     = rnd.nextInt()
  def randomCount    = rnd.nextInt()

  def randomLoggedInUser : Option[(User, Session)] = {
    val pass = randomPassword
    val optU = createRandomUserWithPass(pass)
    if(!(optU isDefined)) { assert(false) }
    val user = optU.get
    User login(user.name, pass)
  }

  def createRandomUserWithPass(pass : String) : Option[User] = {
    val name = rnd.nextString(10)
    User create(name, pass)
  }

  def createRandomUser : Option[User] = {
    val pass = randomPassword
    createRandomUserWithPass(pass)
  }

  def createRandomProject(user: User, customer: Customer) : Option[Project] = {
    val name = randomProjname
    val desc = randomDescript
    Project create(user, customer, name, desc)
  }

  def createRandomCustomer(user: User) : Option[Customer] = {
    val desc = randomDescript
    Customer create(user, randomCustname, desc)
  }

  def createRandomRate(user: User) : Option[Rate] = {
    Rate create(user, randomRatename, randomRate)
  }

  def createRandomRegistration(project: Project, rate: Rate, invoker: User) : Option[Registration] = {
    project addRegistration (randomCount, rate, invoker)
  }

  def cookieHeader(content : String) = {
    addHeader("Cookie", s"timeregSession=$content")
  }

  def entityWithBody = { entityBody : String => HttpEntity(ContentType(MediaTypes.`application/x-www-form-urlencoded`, HttpCharsets.`UTF-8`), entityBody) }

  def entityBody(elements : List[(String, String)]) : String = { 
    def entityElement(element : (String, String)) : String = {
      s"${element._1}=" + URLEncoder.encode(element._2, "UTF-8")
    }

    elements match {
      case x :: xs => {
        val s = new StringBuilder()
        s.append(entityElement(x))
        xs foreach { e : (String, String) => 
          s.append("&").append(entityElement(e))
        }
        s.toString()
      }
      case onFailure => ""
    }
  }
}
