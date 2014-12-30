package data.actors.patterns
import akka.actor.{Actor, ActorLogging, ActorRef, PoisonPill, Props}
import scala.concurrent.duration.{Duration, FiniteDuration}

object IteratorProtocol {
  case object AskingForNextMessage
  case class NextItem[T](item: T)
  case object NoItemsToGive
  case object Shutdown
}

trait IteratorActorSource[T] extends Actor with ActorLogging with RecordDistinctSenders { me =>
  protected def lookupMessageBatch(): Iterator[T]

  final def receive: Receive = waitingForAsk(lookupMessageBatch())

  private[this] def waitingForAsk(iter: Iterator[T]): Receive = {
    case IteratorProtocol.AskingForNextMessage =>
      val listener = sender()
      repondWithPossibleItem(listener, iter)

    case IteratorProtocol.Shutdown =>
      val senders = getDistinctSenders()
      val commander = sender()
      log.debug(s"Shutting down because of message from ${commander} and telling ${senders} to shutdown as well.")
      senders foreach { _ ! IteratorProtocol.Shutdown }
      self ! PoisonPill

    case unhandled =>
      log.warning(s"Unhandled message ${unhandled} sent to ${me.getClass}", unhandled)
  }

  private[this] def repondWithPossibleItem(listener: ActorRef, iter: Iterator[T]): Unit = {
    recordSender(listener)
    if (iter.hasNext) {
      listener ! IteratorProtocol.NextItem(iter.next)
    } else {
      listener ! IteratorProtocol.NoItemsToGive
    }
  }
}

trait IteratorActorSink[T] extends Actor with ActorLogging {
  protected def processMessage(message: T): Unit
  protected def timeoutWhenNoItemsAreFound: Duration

  private case object EndNoItemTimeout

  final def receive: Receive = waitForItemsOrShutdown()

  private[this] def waitForTimeoutAfterNoItemsGiven(): Receive = {
    case EndNoItemTimeout =>
      context.unbecome()

    case IteratorProtocol.Shutdown =>
      val commander = sender()
      stopListening(commander)

    case unhandled =>
      log.warning(s"Got an unhandled message ${unhandled} during waiting after no items were given to us.")
  }

  private[this] def waitForItemsOrShutdown(): Receive = {
    case IteratorProtocol.NextItem(item: T) =>
      log.debug(s"Got item ${item} to process.")
      processMessage(item)

    case IteratorProtocol.NoItemsToGive =>
      import context.dispatcher
      val finite = FiniteDuration(timeoutWhenNoItemsAreFound.length, timeoutWhenNoItemsAreFound.unit)
      context.system.scheduler.scheduleOnce(finite, self, EndNoItemTimeout)
      context.become(waitForTimeoutAfterNoItemsGiven())

    case IteratorProtocol.Shutdown =>
      val commander = sender()
      stopListening(commander)
  }

  private[this] def stopListening(listener: ActorRef): Unit = {
    log.debug(s"Shutting down due to Shutdown message from ${listener}.")
    self ! PoisonPill
  }
}
