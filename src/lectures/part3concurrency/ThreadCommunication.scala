package lectures.part3concurrency

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

  smartProdCons()
}
