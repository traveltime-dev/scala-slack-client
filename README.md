[![Build Status](https://travis-ci.org/igeolise/scala-slack-client.svg?branch=master)](https://travis-ci.org/igeolise/scala-slack-client)
[![Bintray Download](https://api.bintray.com/packages/igeolise/maven/scala-slack-client/images/download.svg) ](https://bintray.com/igeolise/maven/scala-slack-client/_latestVersion)

About
--------------------------------------------------
Minimalistic [Slack](https://slack.com/) client to post messages via [Incoming Webhooks](https://api.slack.com/incoming-webhooks).

Usage
--------------------------------------------------
```scala
import scala.concurrent.Future
import com.igeolise.slack.HttpSlackClient
import com.igeolise.slack.SlackClient._

val token: String = "XXX/YYY/ZZZ"
val response: Future[Either[Error, Unit]] = HttpSlackClient(token).sendMsg(
  notify = Seq(Notify.User("name")),
  msg = "message text",
  attachments = Seq(Attachment("attachment text", Color.Red))
)

import scala.concurrent.Await
import scala.concurrent.duration._

println(Await.result(response, 1.minute))
// Right(())
```

SBT dependency
--------------------------------------------------
Package is available at [Bintray](https://bintray.com/igeolise/maven/scala-slack-client).
Check for the latest version and add to your `build.sbt`:

```
resolvers += Resolver.bintrayRepo("igeolise", "maven")

libraryDependencies += "com.igeolise" %% "scala-slack-client" % "<latest_version>"
```
