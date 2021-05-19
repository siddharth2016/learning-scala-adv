package lectures.part1afp

object PartialFunctions extends App {
  println("Inside Partial Functions")

  val aFussyFuntion: PartialFunction[Int, Int] = {
    case 1 => 1
    case 2 => 2
    case 5 => 5
  }

  println(aFussyFuntion(5))

  println(aFussyFuntion.isDefinedAt(3))

  val lift = aFussyFuntion.lift
  println(lift(10))

  val newFussyFunction = aFussyFuntion.orElse[Int, Int] {
    case 10 => 10
  }

  println(newFussyFunction(10))

  // Anonymous Partial Function

  val aPartialFunction = new PartialFunction[Int, Int] {
    override def apply(v1: Int): Int = v1 match {
      case 1 => 1
      case 2 => 2
      case 3 => 3
    }

    override def isDefinedAt(x: Int): Boolean =
      x == 1 || x == 2 || x == 3
  }

  println(aPartialFunction(1))

  // Dumb Chatbot

  val chatBot: PartialFunction[String, String] = {
    case "Hello" => "Hi"
    case "Tell me something nice" => "Have a nice day"
    case "Bye" => "Bye, See you"
    case _ => "Sorry, I am not trained on this data!"
  }

  scala.io.Source.stdin.getLines().foreach(x => println(chatBot(x)))
}
