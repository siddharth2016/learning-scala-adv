package lectures.part3concurrency

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.{Failure, Success}

object FuturesAndPromises extends App {
  def someValue = 42

  val aFuture = Future {
    someValue
  }

  println(aFuture.value)    // Option[Try[Int]]

  aFuture.onComplete {
    case Success(value) => println("It is a Success " + value)
    case Failure(exception) => println("It is a Failure " + exception)
  }

//  Thread.sleep(3000)
}
