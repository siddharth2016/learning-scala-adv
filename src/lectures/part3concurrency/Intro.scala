package lectures.part3concurrency

import java.util.concurrent.{Executor, Executors}

object Intro extends App {
  println("Hello JVM Concurrency Intro")

  val aThread = new Thread(new Runnable {
    override def run(): Unit = println("Inside Runnable")
  })

  aThread.start()
  aThread.join()

  val threadHello = new Thread(() => (1 to 5).foreach(_ => println("Hello")))
  val threadBye = new Thread(() => (1 to 5).foreach(_ => println("Bye")))

  threadHello.start()
  threadBye.start()
  threadHello.join()
  threadBye.join()

  val pool = Executors.newFixedThreadPool(10)
  pool.execute(() => println("inside pool executor"))
  pool.execute(() => {
    Thread.sleep(1000)
    println("done after 1 sec")
  })
  pool.execute(() => {
    Thread.sleep(1000)
    println("almost done")
    Thread.sleep(1000)
    println("done after 2 sec")
  })

  pool.shutdown()
  println(pool.isShutdown)

  def inceptionThread(maxThreads: Int, i: Int = 1): Thread = new Thread(() => {
    if (i < maxThreads) {
      val newThread = inceptionThread(maxThreads, i+1)
      newThread.start()
      newThread.join()
    }
    println(s"Hello from thread $i")
  })

  inceptionThread(50).start()


}
