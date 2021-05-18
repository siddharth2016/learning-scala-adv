package lectures.part1as

import scala.util.Try

object DarkSugars extends App {
  def someMethod(n: Int): String = "Hello"

  println(someMethod {
    "Hi"
    42
  })

  val exc = Try {
    throw new RuntimeException
  }

  println(List(1,2,3).map { x =>
    x + 1
  })

  // Single Abstract Method
  trait Action {
    def act(x: Int): Int
  }

  val someAction: Action = (x: Int) => 32

  val aThread = new Thread(() => println("runnable"))

  println(1 :: 2 :: 3 :: List())

  class MyStream[T] {
    def -->:(value: T): MyStream[T] = this
  }

  val something: MyStream[Int] = 1 -->: 2 -->: 3 -->: new MyStream[Int]


}
