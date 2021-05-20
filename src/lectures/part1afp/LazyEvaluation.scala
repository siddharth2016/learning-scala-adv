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


}
