package com.igeolise.slack

import com.igeolise.slack.HooksSlackClient.Error
import com.igeolise.slack.dto.{InteractiveMessage, SlackFile}
import com.igeolise.slack.dto.InteractiveMessage.Channel

import scala.concurrent.Future

trait ApiSlackClient {
  val uploadFileUrl = "https://slack.com/api/files.upload"
  val postMessageUrl = "https://slack.com/api/chat.postMessage"
  val multipartFormDataContentType = "multipart/form-data"

  def uploadFile(channels: Seq[Channel], authToken: String, slackFile: SlackFile): Future[Either[Error, Unit]]
  def sendInteractiveMessage(interactiveMessage: InteractiveMessage, authToken: String): Future[Either[Error, Unit]]
}
