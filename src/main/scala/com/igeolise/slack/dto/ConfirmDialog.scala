package com.igeolise.slack.dto

/**
 * [[https://api.slack.com/reference/block-kit/composition-objects#confirm]]
 */
case class ConfirmDialog(
  title: TextObject,
  text: TextObject,
  confirm: TextObject,
  deny: TextObject
)
