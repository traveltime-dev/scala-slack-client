package com.traveltime.slack

import com.traveltime.slack.HooksSlackClient.HookMessage
import org.apache.commons.lang3.exception.ExceptionUtils

import scala.concurrent.Future

trait HooksSlackClient {
  val hooksUrl = "https://hooks.slack.com/services/"
  val jsonContentType = "application/json"

  def sendMsg(hooksMessage: HookMessage, token: String): Future[Either[HooksSlackClient.Error, Unit]]
}

object HooksSlackClient {

  case class HookMessage(notifications: Seq[Notify], msg: String, attachments: Seq[Attachment])

  sealed trait Color { val code: String }
  object Color {
    sealed abstract class PredefinedColor(val code: String) extends Color
    case object Red     extends PredefinedColor("#FF0000")
    case object Green   extends PredefinedColor("#008000")
    case object Gray    extends PredefinedColor("#808080")
    case object Yellow  extends PredefinedColor("#FFFF00")
    case object White   extends PredefinedColor("#FFFFFF")
    case object Black   extends PredefinedColor("#000000")
    case object Cyan    extends PredefinedColor("#00FFFF")
    case object Magenta extends PredefinedColor("#FF00FF")

    case class CustomColor(code: String) extends Color
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
    case class UserId(id: String) extends Notify(s"<@$id>")
    case class UserGroup(groupName: String, groupId: String) extends Notify(s"<!subteam^$groupId|$groupName>")
  }
}
