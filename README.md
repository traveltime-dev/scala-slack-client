About
--------------------------------------------------
Minimalistic [Slack](https://slack.com/) client.

Send an [Incoming Webhooks](https://api.slack.com/incoming-webhooks) message
--------------------------------------------------
```scala
import scala.concurrent.Future
import com.igeolise.slack.SlackHttpClient
import com.igeolise.slack.HooksSlackClient._

val token: String = "XXX/YYY/ZZZ"
val webHooksMessage = HookMessage(
  notifications = Seq(Notify.UserId("W123")),
  msg = "message text",
  attachments = Seq(Attachment("attachment text", Color.Red))
)

val response: Future[Either[Error, Unit]] = SlackHttpClient.sendMsg(webHooksMessage, token)

import scala.concurrent.Await
import scala.concurrent.duration._

println(Await.result(response, 1.minute))
// Right(())
```

Send an [Interactive Message](https://api.slack.com/interactive-messages)
--------------------------------------------------
```scala
import com.igeolise.slack.dto.InteractiveMessage
import com.igeolise.slack.SlackHttpClient
import com.igeolise.slack.dto.InteractiveMessage.{Channel, Block, Divider}

val authToken: String = "secret-auth-token"
val channel: Channel = Channel("channel-id")
val blocks: Vector[Block] = Vector(Divider, Divider, Divider)
val interactiveMessage = InteractiveMessage(channel, blocks)

val response = SlackHttpClient.sendInteractiveMessage(interactiveMessage, authToken)

import scala.concurrent.Await
import scala.concurrent.duration._

println(Await.result(response, 1.minute))
// Right(())
```

SBT dependency
--------------------------------------------------
Check for the latest version and add to your `build.sbt`:

```
libraryDependencies += "com.igeolise" %% "scala-slack-client" % "<latest_version>"
```