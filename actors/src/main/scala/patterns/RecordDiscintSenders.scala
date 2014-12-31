package data.actors
import akka.actor.{Actor, ActorRef}

trait RecordDistinctSenders {
  private[this] val distinctActorRefs = Set.newBuilder[ActorRef]

  def recordSender(who: ActorRef): Unit =
    distinctActorRefs += who

  def getDistinctSenders(): Set[ActorRef] =
    distinctActorRefs.result
}
