package dk.sual.timereg

import akka.actor.Actor
import spray.routing._
import spray.http._
import MediaTypes._


import dk.sual.timereg.model._
import dk.sual.timereg.services._

class ServiceActor 
    extends Actor 
    with Services {

  def actorRefFactory = context

  def receive = runRoute(route)
}

trait Services extends HttpService with PublicServices with PrivateServices {
  val redirects = 
    path("") {
      redirect("/signup", StatusCodes.Found)
    }

  val catchAll = redirect("/signup", StatusCodes.Found)

  val route = {
    redirects ~
    publicRoute ~
    privateRoute ~
    catchAll
  }

}
