package com.igeolise.slack

import com.igeolise.slack.SlackClient._
import org.apache.commons.lang3.exception.ExceptionUtils

import scala.concurrent.Future

@deprecated("Use HooksSlackClient", "1.4.0")
trait SlackClient {
  val hooksUrl = "https://hooks.slack.com/services/"
  val jsonContentType = "application/json"

  def sendMsg(notify: Seq[Notify], msg: String, attachments: Seq[Attachment]): Future[Either[Error, Unit]]
}

object SlackClient {
  sealed abstract class Color(val code: String)
  object Color {
    case object Red    extends Color("#FF0000")
    case object Green  extends Color("#008000")
    case object Gray   extends Color("#808080")
    case object Yellow extends Color("#FFFF00")
  }

  case class Attachment(text: String, color: Color)

  sealed abstract class Error(val show: String)
  object Error {
    case class UnexpectedStatusCode(statusCode: Int, body: String) extends
      Error(s"Unexpected response status code $statusCode with response body '$body'")
    case class Exception(throwable: Throwable) extends
      Error(ExceptionUtils.getStackTrace(throwable))
  }

  sealed abstract class Notify(val formatted: String)
  object Notify {
    case object Channel extends Notify("<!channel>")
    case class UserID(id: String) extends Notify(s"<@$id>")
    case class UserGroup(groupName: String, groupId: String) extends Notify(s"<!subteam^$groupId|$groupName>")
  }
}