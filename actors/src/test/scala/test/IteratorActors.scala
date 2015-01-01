package data.actors
import akka.actor.ActorRef
import scala.concurrent.duration.Duration

class CountingIteratorSource extends IteratorActorSource[Int] {
  private var sentCount = 0
  protected def lookupMessageBatch() = {
    if (sentCount < 2) {
      sentCount += 1
      Iterator.from(0).take(3)
    } else {
      Iterator.empty
    }
  }
}

class CountingIteratorSink(listener: ActorRef, source: ActorRef) extends IteratorActorSink[Int](source) {
  protected def processMessage(message: Int): Unit = listener ! message
  protected lazy val timeoutWhenNoItemsAreFound = Duration("1 second")
}
