package lectures.part1afp

object LazyEvaluation extends App {

  val x: Int = {
    println("Hello x")
    42
  }

  lazy val y: Int = {
    println("Hello y")
    43
  }

  println(y)

  // side effects

  def conditionWithSideEffects: Boolean = {
    println("Bool")
    true
  }

  def simpleCondition: Boolean = false

  lazy val lazyEvaluation = conditionWithSideEffects
  println(if (simpleCondition && lazyEvaluation) "yes" else "no")

  def byNameMethod(n: => Int): Int = {
    lazy val t = n
    println("something")
    t + t + t + 1
  }
  def threadMethod: Int = {
    println("waiting")
    Thread.sleep(1000)
    42
  }

  println(byNameMethod(threadMethod))

  // withFilter

  def lessThan30(x: Int): Boolean = {
    println(s"$x is less than 30?")
    x < 30
  }

  def greaterThan20(x: Int): Boolean = {
    println(s"$x is greater than 20?")
    x > 20
  }

  val numbers = List(1, 2, 25, 40, 23, 5)
  val ls30 = numbers.filter(lessThan30)
  val gt20 = ls30.filter(greaterThan20)
  println(gt20)

  val ls30lazy = numbers.withFilter(lessThan30)
  val gt20lazy = ls30lazy.withFilter(greaterThan20)
  println()
  gt20lazy.foreach(println)

  // for-comprehension with an if guard uses withFilter
  val even = for {
    n <- List(1,2,3) if n % 2 == 0
  } yield n
  // List(1,2,3).withFilter(_ % 2 == 0).map(_)
  println(even)
  println(List(1,2,3).withFilter(_ % 2 == 0).map(x => x))
}
