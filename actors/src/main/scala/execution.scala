package data.actors
import scala.concurrent.ExecutionContext

trait BlockingExecutionContext {
  protected def executionContext: ExecutionContext = scala.concurrent.ExecutionContext.global
}

trait NonBlockingExecutionContext {
  protected def executionContext: ExecutionContext = scala.concurrent.ExecutionContext.global
}
