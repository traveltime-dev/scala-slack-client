package com.traveltime.slack.util

import java.io.File

import com.traveltime.slack.util.RequestHelpers._
import dispatch.url
import org.asynchttpclient.request.body.multipart.{ByteArrayPart, FilePart, StringPart}
import org.scalatest.{FunSpec, Matchers}

class RequestHelpersSpec extends FunSpec with Matchers {

  describe("addBodyParts()") {
    val tempFile = File.createTempFile("prefix", "suffix")
    tempFile.deleteOnExit()

    it("should add multiple body parts to the request") {
      val baseRequest = url("https://traveltimepaltform.com")

      val part1 = new StringPart("name", "value")
      val part2 = new FilePart("filename", tempFile)
      val part3 = new ByteArrayPart("bytes", "bytes-in-disguise".getBytes())

      val result =
        baseRequest
          .addBodyParts(Seq(part1, part2, part3))
          .toRequest
          .getBodyParts

      val expected =
        baseRequest
          .addBodyPart(part1)
          .addBodyPart(part2)
          .addBodyPart(part3)
          .toRequest
          .getBodyParts

      result shouldBe expected
    }
  }
}
