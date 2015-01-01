package data.actors
import org.specs2.specification.Scope
import org.specs2.mutable.Specification
import akka.actor.{Actor, ActorSystem, Props}

object IteratorSpec extends Specification {

  "start off the source and sink, and have the sink get messages" in new context {
    List(source, sink, listener.ref).foreach(_ ! "test")
    listener expectMsgAllOf(0,1,2)
  }

  "start a source, send it messages and interact with it" in new context {
    source.tell(IteratorProtocol.AskingForNextMessage, listener.ref)
    listener expectMsg(IteratorProtocol.NextItem(0))

    source.tell(IteratorProtocol.AskingForNextMessage, listener.ref)
    listener expectMsg(IteratorProtocol.NextItem(1))

    source.tell(IteratorProtocol.AskingForNextMessage, listener.ref)
    listener expectMsg(IteratorProtocol.NextItem(2))

    source.tell(IteratorProtocol.AskingForNextMessage, listener.ref)
    listener expectMsg(IteratorProtocol.NoItemsToGive)
  }

  "start a sink and mock out the source" in new context {
    val source2 = probe()
    val sink2 = testActorRef(new CountingIteratorSink(listener.ref, source2.ref))

    source2 expectMsg(IteratorProtocol.AskingForNextMessage)
    sink2.tell(IteratorProtocol.NextItem(0), source2.ref)

    source2 expectMsg(IteratorProtocol.AskingForNextMessage)
    sink2.tell(IteratorProtocol.NoItemsToGive, source2.ref)

    source2 expectMsg(IteratorProtocol.AskingForNextMessage)
    sink2.tell(IteratorProtocol.NextItem(1), source2.ref)

    source2 expectMsg(IteratorProtocol.AskingForNextMessage)
  }

  trait context extends ActorSpec {
    lazy val listener = probe()
    lazy val source = testActorRef(new CountingIteratorSource)
    lazy val sink = testActorRef(new CountingIteratorSink(listener.ref, source))
  }
}
