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

  case class Or[A, B](a: A, b: B)
  val either = Or(2, "Two")
  val matched4 = either match {
    case Or(q, w) => s"$q is $w"
  }

  println(matched4)

  object gtThan {
    def unapplySeq(l: List[Int]): Option[Seq[Boolean]] = Option(Seq(l.head > l.tail.head))
  }

  object lsThan {
    def unapplySeq(l: List[Int]): Option[Seq[Boolean]] = Some(Seq(l.head < l.tail.head))
  }

  val matched5 = List(20, 30) match {
    case gtThan(_) => "Matches greater than"
    case lsThan(_) => "Matches less than"
    case _ => "Nothing matched"
  }

  println(matched5)

  abstract class Wrapper[T] {
    def isEmpty: Boolean
    def get: T
  }

  object PersonPattern1 {
    def unapply(person: Person): Wrapper[String] = new Wrapper[String] {
      def isEmpty = false
      def get = person.name
    }
  }

  object PersonPattern2 {
    def unapply(person: Person): Wrapper[String] = new Wrapper[String] {
      def isEmpty = false
      def get = person.name
    }
  }

  val matched6 = lilly match {
    case PersonPattern1(n) => "Matches person 1"
    case _ => "Not Matched 1"
  }

  val matched7 = lilly match {
    case PersonPattern2(n) => "Matches person 2"
    case _ => "Not Matched 2"
  }

  println(matched6)
  println(matched7)

}
