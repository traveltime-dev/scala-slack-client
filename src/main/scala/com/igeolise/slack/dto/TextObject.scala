package com.igeolise.slack.dto

import com.igeolise.slack.dto.TextObject.TextType

/**
 * [[https://api.slack.com/reference/block-kit/composition-objects#text]]
 */
case class TextObject(textType: TextType, text: String)

object TextObject {
  abstract sealed class TextType(val textType: String)
  case class PlainText(emojiEnabled: Boolean = false) extends TextType("plain_text")
  case class Mrkdwn(verbatimEnabled: Boolean = false) extends TextType("mrkdwn")
}