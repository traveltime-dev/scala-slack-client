package com.igeolise.slack

import java.nio.charset.Charset

import com.igeolise.slack.HooksSlackClient.HookMessage
import com.igeolise.slack.HttpSlackClient.{PayloadMapper, _}
import com.igeolise.slack.SlackClient.{Attachment, Error, Notify}
import dispatch.{url => Url, _}
import play.api.libs.json.{JsObject, Json}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

object HttpSlackClient {
  def isResponseSuccess(code: Int): Boolean = code >= 200 && code <= 299

  object PayloadMapper {
    def toBody(notify: Seq[Notify], msg: String, attachments: Seq[Attachment]): String = {

      val notifyMapped = notify.map(_.formatted).mkString(" ") + { if(notify.nonEmpty) " " else "" }
      val json = Json.obj(
        "text" -> (notifyMapped + msg),
        "attachments" -> attachments.map { attachment =>
          Json.obj(
            "text" -> attachment.text,
            "color" -> attachment.color.code,
            "mrkdwn_in" -> Seq("text")
          )
        },
        "link_names" -> 1
      )
      Json.stringify(json)
    }

    def toBodyJson(hookMessage: HookMessage): JsObject = {
      val notifyMapped =
        hookMessage.notifications.map(_.formatted).mkString(" ") + { if(hookMessage.notifications.nonEmpty) " " else "" }

      Json.obj(
        "text" -> (notifyMapped + hookMessage.msg),
        "attachments" -> hookMessage.attachments.map { attachment =>
          Json.obj(
            "text" -> attachment.text,
            "color" -> attachment.color.code,
            "mrkdwn_in" -> Seq("text")
          )
        },
        "link_names" -> 1
      )
    }
  }
}
@deprecated("use SlackHttpClient", "1.4.0")
case class HttpSlackClient(token: String) extends SlackClient {

  @deprecated("use SlackHttpClient.sendMsg()", "1.4.0")
  def sendMsg(notify: Seq[Notify], msg: String, attachments: Seq[Attachment]): Future[Either[Error, Unit]] = {
    val body = PayloadMapper.toBody(notify, msg, attachments)
    val url = hooksUrl + token

    sendPost(url, body)
  }

  private def sendPost(url: String, body: String): Future[Either[Error, Unit]] = {
    val request =
      Url(url)
        .POST
        .setBody(body.toString)
        .setContentType(jsonContentType, Charset.defaultCharset())

    Http
      .default(request)
      .either
      .map {
        case Left(t) => Left(Error.Exception(t))
        case Right(r) => if (isResponseSuccess(r.getStatusCode)) Right(())
        else Left(Error.UnexpectedStatusCode(r.getStatusCode, r.getResponseBody))
      }
  }
}


