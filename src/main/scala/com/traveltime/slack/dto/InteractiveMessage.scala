package com.traveltime.slack.dto

import com.traveltime.slack.dto.InteractiveMessage.{Block, Channel}

case class InteractiveMessage[A](channel: Channel, blocks: Vector[Block[A]])

object InteractiveMessage {
  /**
   * This can be either user id or channel id
   */
  case class Channel(id: String)

  sealed abstract class Block[A](val blockType: String)
  case class Divider[A]() extends Block[A]("divider")
  case class Section[A](textObject: TextObject) extends Block[A]("section")
  case class Actions[A](elements: Vector[A]) extends Block[A]("actions")
}
