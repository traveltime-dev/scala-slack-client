package com.igeolise.slack

import java.nio.charset.Charset

import com.igeolise.slack.HttpSlackClient._
import com.igeolise.slack.SlackClient._
import org.apache.commons.lang3.exception.ExceptionUtils

import scala.concurrent.Future

trait SlackClient {
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
    case class User(name: String) extends Notify(s"@$name")
  }
}

case class HttpSlackClient(token: String) extends SlackClient {
  override def sendMsg(notify: Seq[Notify], msg: String, attachments: Seq[Attachment]): Future[Either[Error, Unit]] = {
    import dispatch._

    import scala.concurrent.ExecutionContext.Implicits.global

    val body = PayloadMapper.toBody(notify, msg, attachments)

    val response = Http.default(url(hooksUrl + token)
      .POST
      .setBody(body)
      .setContentType(jsonContentType, Charset.defaultCharset)).either
    response.map {
      case Left(t) => Left(Error.Exception(t))
      case Right(r) => if (isResponseSuccess(r.getStatusCode)) Right(())
                       else Left(Error.UnexpectedStatusCode(r.getStatusCode, r.getResponseBody))
    }
  }
}

object HttpSlackClient {
  val hooksUrl = "https://hooks.slack.com/services/"
  val jsonContentType = "application/json"

  def isResponseSuccess(code: Int): Boolean = code >= 200 && code <= 299

  object PayloadMapper {
    def toBody(notify: Seq[Notify], msg: String, attachments: Seq[Attachment]): String = {
      import play.api.libs.json._

      val notifyMapped = notify.map(_.formatted).mkString(" ") + { if(notify.nonEmpty) " " else "" }
      val json = Json.obj(
        "text" -> (notifyMapped + msg),
        "attachments" -> attachments.map { attachment =>
          Json.obj(
            "text" -> attachment.text,
            "color" -> attachment.color.code
          )
        },
        "link_names" -> 1
      )
      Json.stringify(json)
    }
  }
}