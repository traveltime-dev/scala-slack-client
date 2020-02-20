package com.igeolise.slack

import java.nio.charset.StandardCharsets.UTF_8

import com.igeolise.slack.HooksSlackClient.{Error, HookMessage}
import com.igeolise.slack.dto.InteractiveMessage.Channel
import com.igeolise.slack.dto.{InteractiveMessage, SlackFile}
import com.igeolise.slack.json.InteractiveMessageWrites._
import com.igeolise.slack.util.PayloadMapper
import com.igeolise.slack.util.RequestHelpers._
import dispatch.{url => Url}
import org.asynchttpclient.request.body.multipart.{FilePart, StringPart}
import play.api.libs.json.{JsValue, Json}

import scala.concurrent.Future

object SlackHttpClient extends HooksSlackClient with ApiSlackClient {

  def sendMsg(hooksMessage: HookMessage, token: String): Future[Either[Error, Unit]] = {
    val body = PayloadMapper.toBodyJson(hooksMessage)
    val url = hooksUrl + token

    sendPost(url, body, Map.empty)
  }

  def sendInteractiveMessage(interactiveMessage: InteractiveMessage, authToken: String): Future[Either[Error, Unit]] =
    sendPost(
      postMessageUrl,
      Json.toJson(interactiveMessage),
      Map("Authorization" -> Seq(authToken))
    )

  private def sendPost(url: String, body: JsValue, headers: Map[String, Seq[String]]): Future[Either[Error, Unit]] =
    Url(url)
      .POST
      .setBody(body.toString)
      .setHeaders(headers)
      .setContentType(jsonContentType, UTF_8)
      .send

  def uploadFile(channels: Seq[Channel], authToken: String, slackFile: SlackFile): Future[Either[Error, Unit]] = {

    val requiredParts = Seq(
      new FilePart("file", slackFile.file),
      new StringPart("channels", channels.map(_.id).mkString(","))
    )

    val allParts = requiredParts ++ getOptionalSlackFileParts(slackFile)

    Url(uploadFileUrl)
      .POST
      .addBodyParts(allParts)
      .setHeaders(Map("Authorization" -> Seq(authToken)))
      .setContentType(multipartFormDataContentType, UTF_8)
      .send
  }

  def getOptionalSlackFileParts(slackFile: SlackFile): Seq[StringPart] =
    Seq(
      toPart("filename", slackFile.maybeFileName),
      toPart("filetype", slackFile.maybeFileType),
      toPart("thread_ts", slackFile.maybeThreadTs),
      toPart("title", slackFile.maybeTitle),
      toPart("initial_comment", slackFile.maybeInitialComment)
    ).collect{ case Some(part) => part }
}
