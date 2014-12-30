package data.actors
import scala.concurrent.ExecutionContext

trait BlockingExecutionContext {
  protected def executionContext: ExecutionContext
}

trait NonBlockingExecutionContext {
  protected def executionContext: ExecutionContext
}
