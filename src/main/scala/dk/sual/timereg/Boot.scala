package dk.sual.timereg

import akka.actor.{ActorSystem, Props}
import akka.io.IO
import spray.can.Http
import akka.pattern.ask
import akka.util.Timeout
import scala.concurrent.duration._

import dk.sual.timereg.db.DbAccess

object Boot extends App with DbAccess {

  implicit val system = ActorSystem("timereg")

  val service = system.actorOf(Props[ServiceActor], "service")

  if(Settings.db.dbReset) { resetDb }

  implicit val timeout = Timeout(5.seconds)

  IO(Http) ? Http.Bind(service, interface = "localhost", port = 8080)
}

