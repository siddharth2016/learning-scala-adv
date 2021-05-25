package lectures.part3concurrency

import scala.collection.mutable
import scala.language.postfixOps
import scala.util.Random

object ThreadCommunication extends App {

  class SimpleContainer {
    private var value: Int = 0

    def isEmpty: Boolean = value == 0
    def get: Int = {
      val result = value
      value = 0
      result
    }
    def set(newValue: Int): Unit = value = newValue
  }

  def naiveProCons = {
    val container = new SimpleContainer

    val consumer = new Thread(() => {
      println("consumer is waiting...")
      while(container.isEmpty){
        println("consumer actively waiting")
      }
      println("Consumer got value of " + container.get)
    })

    val producer = new Thread(() => {
      println("producer computing values...")
      Thread.sleep(10)
      println("producer doing some hard work here...")
      container.set(42)
    })

    consumer.start()
    producer.start()
  }

//  naiveProCons

  def smartProdCons(): Unit = {
    val container = new SimpleContainer

    def consumer = new Thread( () => {
      println("consumer waiting...")
      container.synchronized {
        container.wait()
      }
      println("consumer got the value " + container.get)
    })

    def producer = new Thread( () => {
      println("producer working hard here...")
      container.synchronized {
        Thread.sleep(2000)
        println("producer sets some value")
        container.set(42)
        container.notify()
      }
    })

    consumer.start()
    producer.start()
  }

//  smartProdCons()

  def prodConsLargeBuffer = {
    val buffer: mutable.Queue[Int] = new mutable.Queue[Int]
    val cap: Int = 3

    def consumer = new Thread(() => {
      val random = new Random()

      while(true) {
        buffer.synchronized {
          if (buffer.isEmpty) {
            println("consumer is waiting...")
            buffer.wait()
          }
          val x = buffer.dequeue()
          println("consumer consumed  " + x)
          buffer.notify()
        }

        Thread.sleep(random.nextInt(500))
      }
    })

    def producer = new Thread(() => {
      val random = new Random()
      var i = 0

      while(true) {
        buffer.synchronized {
          if (buffer.size == cap) {
          println("producer is working hard here...")
          buffer.wait()
        }

        println("producer produced " + i)
        buffer.enqueue(i)
          buffer.notify()
        i += 1
      }
      Thread.sleep(random.nextInt(500))
      }
    })

    producer.start()
    consumer.start()
  }

  prodConsLargeBuffer
}
