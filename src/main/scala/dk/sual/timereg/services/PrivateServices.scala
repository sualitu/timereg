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


trait PrivateServices extends TimeregService {
  def privateRoute =
    // Collections
    path("customers") {
      get {
        withCookie{ s : Session => html(CustomersPage(Some(s), NoRequest())) }
      }
    } ~
    path("projects") {
      get {
        withCookie{ s : Session => html(ProjectsPage(Some(s), NoRequest())) }
      }
    } ~
    path("rates") {
      get {      
        withCookie{ s : Session => html(RatesPage(Some(s), NoRequest())) }
      }
    } ~
    // Elements
    pathPrefix("registration") {
      path("create") {
        formFields('count, 'rate, 'project) { (count, rate, project) =>
          val action = userAction { u : User => u addRegistration((count.toIntOpt getOrElse 0), (rate.toIntOpt getOrElse 0), (project.toIntOpt getOrElse 0)) } (s"/project/$project")
          withCookie(action)
        }
      } ~
      path(IntNumber / "delete") { id =>
        val ret : String =
          (Registration fromId(id)) map (_.project) getOrElse None match {
            case Some(p) => s"/project/${p.id}"
            case None => "/projects"
          }
        val action = userAction { u : User => u removeRegistration id } ("")
        withCookie(action)
      }
    } ~
    pathPrefix("rate") {
      path("create") {
        formFields('rate, 'name) { (rate, name) =>
          val action = userAction { u : User => u addRate(name, rate.toIntOpt getOrElse 0) } ("/rates")
          withCookie(action)
        }
      } ~
      path(IntNumber / "delete") { id =>
        val action = userAction { u : User => u removeRate id } ("/rates")
        withCookie(action)
      } 
    } ~
    pathPrefix("customer") {
      path("create") {
        formFields('name, 'description) { (name, description) =>
          val action = userAction { u : User => u addCustomer(name, Some(description)) } ("/customers")
          withCookie(action)
        }
      } ~
      pathPrefix(IntNumber) { id =>
        pathEnd {
          withCookie{ s : Session => html(CustomerPage(Some(s), CustomerPageRequest(Customer fromId(id)))) }
        } ~
        path("delete") {
          post {
            val action = userAction { u : User => u removeCustomer id } ("/customers")
            withCookie(action)
          }
        }
      }
    } ~
    pathPrefix("project") {
      path("create") {
        formFields('name, 'description, 'customer, 'ret) { (name, description, customer, ret) =>
          val action = userAction { u : User => u addProject((customer.toIntOpt) getOrElse 0, name, Some(description)) } (ret)
          withCookie(action)
        }
      } ~
      pathPrefix(IntNumber) { id =>
        path("delete") {
          post {
            val action = userAction { u : User => u removeProject id } ("/projects")
            withCookie(action)
          }
        } ~
        pathEnd {
          withCookie{ s : Session => html(ProjectPage(Some(s), ProjectPageRequest(Project fromId(id)))) }
        } 
      }
    } ~
    // Misc
    path("invoice" / IntNumber) { id =>
      get {
        withCookie { s : Session =>  html(InvoicePage(Some(s), ProjectPageRequest(Project fromId(id)))) }
      }
    } ~
    path("signout") {
      get {
        val action = { s : Session =>
          s.invalidate
          redirectSignin
        }
        withCookie(action)
      }
    }
}
