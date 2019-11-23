package com.igeolise.slack.json

import com.igeolise.slack.dto.Button.{ActionId, Danger, Primary}
import com.igeolise.slack.dto.{Button, InteractiveMessage, TextObject}
import com.igeolise.slack.dto.InteractiveMessage.{Actions, Channel, Divider, Section}
import com.igeolise.slack.dto.TextObject.{Mrkdwn, PlainText}
import org.scalatest.{FunSpec, Matchers}
import play.api.libs.json.Json
import com.igeolise.slack.json.InteractiveMessageWrites._

import scala.io.Source

class InteractiveMessageWritesSpec extends FunSpec with Matchers {

  describe("Interactive Slack messages serialization") {

    it("should serialize interactive slack message") {

      val section = Section(TextObject(Mrkdwn(), "Hello, I would like to do some things"))
      val divider = Divider
      val section2 = Section(TextObject(PlainText(), "Can I do this?"))
      val approveButton = Button(
        textObject = TextObject(PlainText(true), "Approve"),
        actionId = ActionId("approve_for_123"),
        style = Some(Primary),
        url = None,
        value = Option("click_me_123")
      )
      val denyButton = Button(
        textObject = TextObject(PlainText(true), "Deny"),
        actionId = ActionId("deny_for_123"),
        style = Some(Danger),
        url = None,
        value = Option("click_me_123")
      )
      val actions = Actions(Vector(approveButton, denyButton))

      val blocks = Vector(section, divider, section2, actions)
      val interactiveSlackMessage = InteractiveMessage(Channel("test_channel_Id"), blocks)

      val content = Source.fromURL(getClass.getClassLoader.getResource("json/interactiveMessage.json")).mkString

      val expected = Json.parse(content)
      val result = Json.toJson(interactiveSlackMessage)

      result shouldBe expected
    }
  }
}
