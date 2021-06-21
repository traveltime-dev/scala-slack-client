package com.traveltime.slack.json

import com.traveltime.slack.dto.InteractiveMessage._
import com.traveltime.slack.dto.TextObject.{Mrkdwn, PlainText, TextType}
import com.traveltime.slack.dto.{Button, ConfirmDialog, InteractiveMessage, TextObject}
import play.api.libs.functional.syntax._
import play.api.libs.json._

object InteractiveMessageWrites {

  val mrkdwnTextWrites: Mrkdwn => JsObject = (mrkdwn: Mrkdwn) => JsObject(Map(
    "type" -> JsString(mrkdwn.textType),
    "verbatim" -> JsBoolean(mrkdwn.verbatimEnabled)
  ))

  val plainTextWrites: PlainText => JsObject = (plainText: PlainText) => JsObject(Map(
    "type" -> JsString(plainText.textType),
    "emoji" -> JsBoolean(plainText.emojiEnabled)
  ))

  val textTypeWrites: TextType => JsObject = {
    case plainText: PlainText => plainTextWrites(plainText)
    case mrkdwn: Mrkdwn => mrkdwnTextWrites(mrkdwn)
  }

  implicit val textObjectWrites: Writes[TextObject] = (textObject: TextObject) =>
    textTypeWrites(textObject.textType) ++ JsObject(Map("text" -> JsString(textObject.text)))

  def sectionWrites[A]: Writes[Section[A]] = (section: Section[A]) => JsObject(Map(
    "type" -> JsString(section.blockType),
    "text" -> textObjectWrites.writes(section.textObject)
  ))

  val confirmDialogWrites: Writes[ConfirmDialog] = (
    (__ \ "title").write[TextObject] and
    (__ \ "text").write[TextObject] and
    (__ \ "confirm").write[TextObject] and
    (__ \ "deny").write[TextObject]
  )(unlift(ConfirmDialog.unapply))

  implicit val buttonWrites: Writes[Button] = (button: Button) => JsObject(
    Map(
      "type" -> Some(JsString("button")),
      "text" -> Some(textObjectWrites.writes(button.textObject)),
      "action_id" -> Some(JsString(button.actionId.id)),
      "url" -> button.url.map(JsString),
      "value" -> button.value.map(JsString),
      "style" -> button.style.map(style => JsString(style.literal)),
      "confirm" -> button.confirm.map(confirmDialogWrites.writes)
    ).collect { case (key, Some(value)) => (key, value) }
  )

  def actionsWrites[A: Writes]: Writes[Actions[A]] = (actions: Actions[A]) => {
    val w = implicitly[Writes[A]]
    JsObject(Map(
      "type" -> JsString(actions.blockType),
      "elements" -> JsArray(actions.elements.map(w.writes))
    ))
  }

  def dividerWrites[A]: Writes[Divider[A]] = _ =>
    JsObject(Map("type" -> JsString("divider")))

  implicit def blockWrites[A: Writes]: Writes[Block[A]] = Writes[Block[A]] {
    case b: Divider[A] => dividerWrites.writes(b)
    case b: Section[A] => sectionWrites.writes(b)
    case b: Actions[A] =>
      val w = implicitly[Writes[A]]
      actionsWrites(w).writes(b)
  }

  implicit def interactiveMessageWrites[A: Writes]: Writes[InteractiveMessage[A]] =
    (
      (__ \ "channel").write[String].contramap[Channel](_.id) and
        (__ \ "blocks").write[Vector[Block[A]]]
      ) (unlift(InteractiveMessage.unapply[A]))
}
