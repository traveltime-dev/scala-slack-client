package com.igeolise.slack.dto

import java.io.File

case class SlackFile(
  file: File,
  maybeFileName: Option[String],
  maybeFileType: Option[String],
  maybeInitialComment: Option[String],
  maybeThreadTs: Option[String],
  maybeTitle: Option[String]
)

