package com.igeolise.slack.util

import com.igeolise.slack.HooksSlackClient.Error
import dispatch.{Http, Req, _}
import org.asynchttpclient.request.body.multipart.{Part, StringPart}

import scala.concurrent.ExecutionContext.Implicits.global

object RequestHelpers {

  implicit class ReqExt(req: Req) {

    def addBodyParts(parts: Seq[Part]): Req =
      parts.foldLeft(req)((requestAcc, part) => requestAcc.addBodyPart(part))

    def send: Future[Either[Error, Unit]] =
      Http
        .default(req)
        .either
        .map {
          case Left(t) => Left(Error.Exception(t))
          case Right(r) => if (isResponseSuccess(r.getStatusCode)) Right(())
          else Left(Error.UnexpectedStatusCode(r.getStatusCode, r.getResponseBody))
        }
  }

  def isResponseSuccess(code: Int): Boolean = code >= 200 && code <= 299

  def toPart(name: String, maybeField: Option[String]): Option[StringPart] =
    maybeField.map(field => new StringPart(name, field))
}
