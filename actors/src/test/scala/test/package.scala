package data
import akka.actor.{Actor, ActorSystem}
import org.specs2.specification.Scope
import akka.testkit.{TestActorRef, TestKit, TestProbe, ImplicitSender}

package object actors {
  lazy val ActorsTestSystem = ActorSystem("data-actors-test")
}

package actors {
  class ActorSpec extends TestKit(ActorsTestSystem) with Scope with ImplicitSender {
    def probe() = TestProbe()(ActorsTestSystem)
    def testActorRef[T <: Actor]()(implicit m: Manifest[T]) = TestActorRef(m, ActorsTestSystem)
    def testActorRef[T <: Actor](factory: => T)(implicit m: Manifest[T]) = TestActorRef(factory)(m, ActorsTestSystem)
  }
}
