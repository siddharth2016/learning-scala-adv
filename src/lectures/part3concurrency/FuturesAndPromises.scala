package lectures.part3concurrency

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.concurrent.{Await, Future, Promise}
import scala.language.postfixOps
import scala.util.{Failure, Random, Success, Try}

object FuturesAndPromises extends App {
  def someValue = 42

  val aFuture = Future {
    someValue
  }

//  println(aFuture.value)    // Option[Try[Int]]

//  aFuture.onComplete {
//    case Success(value) => println("It is a Success " + value)
//    case Failure(exception) => println("It is a Failure " + exception)
//  }

//  Thread.sleep(3000)

  case class Profile(id: String, name: String) {
    def poke(anotherProfile: Profile) = println(s"${this.name} poke ${anotherProfile.name}")
  }

  object SocialNetwork {
    val names = Map(
      "id1" -> "Sid",
      "id2" -> "Abhi",
      "id3" -> "Aya"
    )

    val friends = Map(
      "id1" -> "id2"
    )

    val random = new Random()

    def fetchProfile(id: String): Future[Profile] = Future {
      Thread.sleep(random.nextInt(300))
      Profile(id, names(id))
    }

    def fetchFriendsProfile(profile: Profile): Future[Profile] = Future {
      Thread.sleep(random.nextInt(300))
      val friendId = friends(profile.id)
      Profile(friendId, names(friendId))
    }
  }

  val sid = SocialNetwork.fetchProfile("id1")
  // map, flatMap, filter
  val name = sid.map(x => x.name)
  val friend = sid.flatMap(x => SocialNetwork.fetchFriendsProfile(x))
  val filterabhi = friend.filter(x => x.name.startsWith("A"))

  val err = SocialNetwork.fetchProfile("no name").recover {
    case e: Throwable => SocialNetwork.fetchProfile("id3")
  }

  val err2 = SocialNetwork.fetchProfile("no name").recoverWith {
    case e: Throwable => SocialNetwork.fetchProfile("id4")
  }

  val err3 = SocialNetwork.fetchProfile("no name").fallbackTo(SocialNetwork.fetchProfile("id4"))

//  Thread.sleep(1000)
//
//  println(err)
//  println(sid)
//  println(err2)
//  println(err3)

  // Using Await.result to wait till future computation is in the play

  case class User(name: String)
  case class Transaction(user: User, merchant: String, amount: Int, status: String)

  object BankingApp {
    val names = "Something"

    def fetchUser(name: String): Future[User] = Future {
      Thread.sleep(500)
      User(name)
    }

    def createTransaction(user: User, merchant: String, amount: Int): Future[Transaction] = Future {
      Thread.sleep(1000)
      Transaction(user, merchant, 1000, "SUCCESS")
    }

    def purchase(per1: String, per2: String, amt: Int): String = {
      val status = for {
        u <- fetchUser(per1)
        t <- createTransaction(u, per2, amt)
      } yield t.status

      Await.result(status, 2 seconds)
    }
  }

//  println(BankingApp.purchase("Sid", "Abhi", 1000))
  // Using Promises

  val promise = Promise[Int]()
  val future = promise.future

  // Consumer thread
  future.onComplete {
    case Success(res) => println("[consumer] Done getting value " + res)
  }

  // Producer thread
  val producer = new Thread(() => {
    println("[producer] starting producing numbers...")
    Thread.sleep(500)
    promise.success(42)
    println("[producer] produced the number")
  })

//  producer.start()

  // Exercises

  /*
  1) Fulfill a future Immediately with a value
  2) inSeq(fa, fb)
  3) first(fa, fb) => new future with the first value of the furture
  4) last(fa, fb) => new future with the last value of the future
  5) retryUntil[T](action: () => Future[T], condition: T => Boolean): Future[T]
   */


  // 1
  println("ex 1")
  def fulfillImmediately[T](value: T): Future[T] = Future(value)
  println(fulfillImmediately(5).value)

  // 2
  println("ex 2")
  def inSeq[A, B](fa: Future[A], fb: Future[B]): Future[B] =
    fa.flatMap(_ => fb)

  // 3
  println("ex 3")
  def first[A](fa: Future[A], fb: Future[A]): Future[A] = {
    val promise = Promise[A]
    fa.onComplete(promise.tryComplete)
    fb.onComplete(promise.tryComplete)
    promise.future
  }

  // 4
  println("ex 4")
  def last[A](fa: Future[A], fb: Future[A]): Future[A] = {
    val bothPromise = Promise[A]
    val lastPromise = Promise[A]
    val checkAndComplete = (result: Try[A]) => {
      if(!bothPromise.tryComplete(result))
        lastPromise.complete(result)
    }

    fa.onComplete(checkAndComplete)
    fb.onComplete(checkAndComplete)

    lastPromise.future
  }

  // 5
  println("ex 5")
  def retryUntil[A](action: () => Future[A], condition: A => Boolean): Future[A] = {
    action()
      .filter(condition)
      .recoverWith {
        case _ => retryUntil(action, condition)
      }
  }



}
