package data.actors
import org.specs2.specification.Scope
import org.specs2.mutable.Specification
import akka.actor.{Actor, ActorSystem, Props}

object RecordDistinctSendersSpec extends Specification {

  "only record things once" in new context {
    getDistinctSenders() must beEmpty
    recordSender(actor1)

    getDistinctSenders() must contain(exactly(actor1))
    recordSender(actor1)
    getDistinctSenders() must contain(exactly(actor1))

    recordSender(actor2)
    getDistinctSenders() must contain(exactly(actor1, actor2))
  }

  trait context extends Scope with RecordDistinctSenders {
    val system = ActorSystem("record-distinct")
    lazy val actor1 = system.actorOf(Props[MyActor])
    lazy val actor2 = system.actorOf(Props[MyActor])
  }

  class MyActor extends Actor {
    def receive = {
      case _ => println("received unknown message")
    }
  }
}
