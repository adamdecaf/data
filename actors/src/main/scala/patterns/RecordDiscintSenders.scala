package data.actors.patterns
import akka.actor.{Actor, ActorRef}

trait RecordDistinctSenders {
  this: Actor =>

  private[this] val distinctActorRefs = Set.newBuilder[ActorRef]

  def recordSender(who: ActorRef): Unit =
    distinctActorRefs += who

  def getDistinctSenders(): Set[ActorRef] =
    distinctActorRefs.result
}
