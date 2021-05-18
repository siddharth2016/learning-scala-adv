package lectures.part1as

object AdvPatterMatching extends App {
  val l = List(1)
  val matched = l match {
    case head :: Nil => println(s"the only element is $head")
    case _ => println("Nothing")
  }
  println(matched)

  class Person(val name: String, val age: Int)

  object Person {
    def unapply(person: Person): Option[(String, Int)] = Some((person.name, person.age))

    def unapply(age: Int): Option[String] =
      Some(if (age < 21) "minor" else "major")
  }

  val lilly = new Person("Lilly", 20)
  val matched1 = lilly match {
    case Person(n, a) => s"Person is $n, age $a"
    case _ => "Nothing Matched"
  }

  val matched2 = lilly.age match {
    case Person(status) => s"Hi $status"
  }

  println(matched1)
  println(matched2)

  // Exercise

  object greaterThan {
    def unapply(a: Int): Boolean = a > 10
  }

  object lessThan {
    def unapply(a:Int): Boolean = a < 10
  }

  val v1 = 10
  val v2 = 20
  val matched3 = v2 match {
    case greaterThan() => s"greater than"
    case lessThan() => s"is less than"
    case _ => "Both values are equal"
  }
  println(matched3)
}
