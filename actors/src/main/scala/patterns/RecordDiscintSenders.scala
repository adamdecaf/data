package data.actors
import akka.actor.{Actor, ActorRef}

trait RecordDistinctSenders {
  private[this] val distinctActorRefs = Set.newBuilder[ActorRef]

  final def recordSender(who: ActorRef): Unit = distinctActorRefs += who
  final def getDistinctSenders(): Set[ActorRef] = distinctActorRefs.result
}
