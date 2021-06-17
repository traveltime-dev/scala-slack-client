package com.traveltime.slack

import java.io.File

import com.traveltime.slack.HooksSlackClient.Color.{Gray, Green}
import com.traveltime.slack.HooksSlackClient.Notify.{Channel, UserGroup, UserId}
import com.traveltime.slack.HooksSlackClient.{Attachment, HookMessage}
import com.traveltime.slack.dto.SlackFile
import com.traveltime.slack.util.{PayloadMapper, RequestHelpers}
import org.asynchttpclient.request.body.multipart.StringPart
import org.scalacheck.Gen
import org.scalatest.{FunSpec, Matchers}
import org.scalatestplus.scalacheck.ScalaCheckDrivenPropertyChecks

class SlackHttpClientTest extends FunSpec with Matchers with ScalaCheckDrivenPropertyChecks {

  describe("HttpSlackClient") {
    it("treat 2xx response as success") {
      val successCodeGen = Gen.choose(200, 299)

      forAll(successCodeGen) { code =>
        RequestHelpers.isResponseSuccess(code) shouldEqual true
      }
    }

    it("treat non 2xx response as failure") {
      val failureCodeGen = for {
        bellow <- Gen.choose(-1000, 199)
        above  <- Gen.choose(300, 1000)
        code   <- Gen.oneOf(bellow, above)
      } yield code

      forAll(failureCodeGen) { code =>
        RequestHelpers.isResponseSuccess(code) shouldEqual false
      }
    }
  }

  describe("PayloadMapper") {
    it("should map to json string") {
      val message = HookMessage(
        notifications = Seq(Channel, UserId("W123"), UserGroup("dev-ops", "SGQKH63CF")),
        msg = "some message text",
        attachments = Seq(Attachment("some attachment text 1", Green), Attachment("some attachment text 2", Gray))
      )
      val mapped = PayloadMapper.toBodyJson(message).toString

      val expected = """{"text":"<!channel> <@W123> <!subteam^SGQKH63CF|dev-ops> some message text",""" +
                     """"attachments":[""" +
                     """{"text":"some attachment text 1","color":"#008000","mrkdwn_in":["text"]},""" +
                     """{"text":"some attachment text 2","color":"#808080","mrkdwn_in":["text"]}""" +
                     """],"link_names":1}"""
      mapped shouldEqual expected
    }
  }

  describe("getOptionalSlackFileParts()") {
    it("should create existing parts collection out of SlackFile") {

      val slackFile = SlackFile(
        file = new File("temp"),
        maybeFileName = None,
        maybeFileType = Some("csv"),
        maybeInitialComment = None,
        maybeThreadTs = None,
        maybeTitle = Some("best-title")
      )

      val expectedFileTypePart = new StringPart("filetype", "csv")
      val expectedTitlePart = new StringPart("title", "best-title")

      val result = SlackHttpClient.getOptionalSlackFileParts(slackFile).map(_.toString)
      val expected = Seq(expectedFileTypePart, expectedTitlePart).map(_.toString)

      result should contain theSameElementsAs expected
    }
  }
}
