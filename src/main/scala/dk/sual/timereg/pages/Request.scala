package dk.sual.timereg.pages.request

import dk.sual.timereg.model._
import java.util.UUID

sealed abstract class Request
case class NoRequest() extends Request
case class ProjectPageRequest(project: Option[Project]) extends Request
case class CustomerPageRequest(customer: Option[Customer]) extends Request

