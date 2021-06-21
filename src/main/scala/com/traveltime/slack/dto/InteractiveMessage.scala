package com.traveltime.slack.dto

import com.traveltime.slack.dto.InteractiveMessage.{Block, Channel}
import play.api.libs.json.JsValue

case class InteractiveMessage(channel: Channel, blocks: Vector[Block])

object InteractiveMessage {
  /**
   * This can be either user id or channel id
   */
  case class Channel(id: String)

  trait Element {
    def asJson: JsValue
  }

  sealed abstract class Block(val blockType: String)
  case object Divider extends Block("divider")
  case class Section(textObject: TextObject) extends Block("section")
  case class Actions(elements: Vector[Element]) extends Block("actions")
}
