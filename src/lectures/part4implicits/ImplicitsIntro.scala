package lectures.part4implicits

object ImplicitsIntro extends App {

  case class Person(name: String) {
    def greet = s"Hi, $name"
  }

  implicit def createPerson(str: String): Person = Person(str)

  println("Sid")  // no implicit conversion
  println("Sid".greet)

  def sum(x: Int)(implicit amt: Int) = x + amt

//  implicit val defAmt: Int = 10
  implicit val y: Int = 11

  println(sum(2)(2))  // no implicit conversion
  println(sum(2))

}
