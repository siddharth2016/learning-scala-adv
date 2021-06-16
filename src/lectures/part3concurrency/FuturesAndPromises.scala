package lectures.part3concurrency

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.concurrent.{Await, Future, Promise}
import scala.language.postfixOps
import scala.util.{Failure, Random, Success}

object FuturesAndPromises extends App {
  def someValue = 42

  val aFuture = Future {
    someValue
  }

  println(aFuture.value)    // Option[Try[Int]]

  aFuture.onComplete {
    case Success(value) => println("It is a Success " + value)
    case Failure(exception) => println("It is a Failure " + exception)
  }

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

  producer.start()

  // Exercises

}
