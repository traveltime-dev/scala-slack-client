package com.traveltime.slack

import com.traveltime.slack.HooksSlackClient.Error
import com.traveltime.slack.dto.{InteractiveMessage, SlackFile}
import com.traveltime.slack.dto.InteractiveMessage.Channel
import play.api.libs.json.Writes

import scala.concurrent.Future

trait ApiSlackClient {
  val uploadFileUrl = "https://slack.com/api/files.upload"
  val postMessageUrl = "https://slack.com/api/chat.postMessage"
  val multipartFormDataContentType = "multipart/form-data"

  def uploadFile(channels: Seq[Channel], authToken: String, slackFile: SlackFile): Future[Either[Error, Unit]]
  def sendInteractiveMessage[A : Writes](interactiveMessage: InteractiveMessage[A], authToken: String): Future[Either[Error, Unit]]
}
