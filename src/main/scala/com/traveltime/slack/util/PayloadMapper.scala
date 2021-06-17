package com.traveltime.slack.util

import com.traveltime.slack.HooksSlackClient.HookMessage
import play.api.libs.json.{JsObject, Json}

object PayloadMapper {

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