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
