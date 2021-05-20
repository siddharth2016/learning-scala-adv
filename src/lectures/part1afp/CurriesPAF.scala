package lectures.part1afp

object CurriesPAF extends App {

  val superAdder: Int => Int => Int = x => y => x + y

  val add3 = superAdder(3)
  println(add3(1))

  def superAdder2(x: Int)(y: Int): Int = x + y

  val add4: Int => Int = superAdder2(3)

  def inc(x: Int): Int = x + 1
  println(List(1,2,3).map(inc))   // ETA-Expansion

  val simpleAddFunction: (Int, Int) => Int = (x, y) => x + y
  def simpleAddMethod(x: Int, y: Int): Int = x + y
  def curriedAddMethod(x: Int)(y: Int): Int = x + y

  // create add7 method with as many possible implementation as we can

  val add7 = (y: Int) => simpleAddFunction(7, y)
  val add7_2 = (y: Int) => simpleAddMethod(7, y)
  val add7_3 = curriedAddMethod(7) _
  val add7_4 = curriedAddMethod(7)(_)
  val add7_5 = simpleAddFunction.curried(7)
  val add7_6 = simpleAddFunction(7, _: Int)
  val add7_7 = simpleAddMethod(7, _:Int)

  def concatenator(a: String, b: String, c: String): String = a + b + c

  val greeting = concatenator("Hello, I am ", _: String, ", How wre you?")
  val confused = concatenator("Hello ", _: String, _: String)

  println(greeting("Siddharth"))
  println(confused("I am Sid ", "and I am confused"))

  // Curried Formatter

  def formatter(fmt: String, l: List[Float]): Unit =
    l.foreach(x => println(fmt.format(x)))

  val f42 = formatter("%4.2f", _: List[Float])
  val f86 = formatter("%8.6f", _: List[Float])
  val f1412 = formatter("%14.12f", _: List[Float])

  f42(List(1.2f, 3.2f, 4.5f))
  f86(List(1.2f, 3.2f, 4.5f))
  f1412(List(1.2f, 3.2f, 4.5f))


}

