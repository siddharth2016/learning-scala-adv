package lectures.part1afp

object FunctionalCollection extends App {
  /*
    Exercise, build a functional set. Set are actually implemented using functions in scala.
   */

  trait MySet[A] extends (A => Boolean) {
    def contains(elem: A): Boolean
    def +(elem: A): MySet[A]
    def ++(newSet: MySet[A]): MySet[A]

    def map[B](func: A => B): MySet[B]
    def flatMap[B](func: A => MySet[B]): MySet[B]
    def filter(func: A => Boolean): MySet[A]
    def foreach(func: A => Unit): Unit
  }

}
