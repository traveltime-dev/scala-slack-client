package com.igeolise.slack

import com.igeolise.slack.HooksSlackClient.Error
import com.igeolise.slack.dto.InteractiveMessage

import scala.concurrent.Future

trait ApiSlackClient {
  def sendInteractiveMessage(interactiveMessage: InteractiveMessage, authToken: String): Future[Either[Error, Unit]]
}
