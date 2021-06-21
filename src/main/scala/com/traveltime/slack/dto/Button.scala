package com.traveltime.slack.dto

import com.traveltime.slack.dto.Button.{ActionId, ButtonStyle}
import com.traveltime.slack.dto.InteractiveMessage.Element
import com.traveltime.slack.json.InteractiveMessageWrites.{confirmDialogWrites, textObjectWrites}
import play.api.libs.json.{JsObject, JsString}

/**
 * [[https://api.slack.com/reference/block-kit/block-elements#button]]
 */
case class Button(
  textObject: TextObject,
  actionId: ActionId,
  style: Option[ButtonStyle] = None,
  url: Option[String] = None,
  value: Option[String] = None,
  confirm: Option[ConfirmDialog] = None
) extends Element {
  override def asJson = JsObject(
    Map(
      "type"      -> Some(JsString("button")),
      "text"      -> Some(textObjectWrites.writes(textObject)),
      "action_id" -> Some(JsString(actionId.id)),
      "url"       -> url.map(JsString),
      "value"     -> value.map(JsString),
      "style"     -> style.map(style => JsString(style.literal)),
      "confirm"   -> confirm.map(confirmDialogWrites.writes)
    ).collect { case (key, Some(value)) => (key, value) }
  )
}

object Button {
  case class ActionId(id: String)

  sealed abstract class ButtonStyle(val literal: String)
  case object Primary extends ButtonStyle("primary")
  case object Danger extends ButtonStyle("danger")
}
