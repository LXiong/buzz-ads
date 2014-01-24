package com.buzzinate.advertise.async

import com.buzzinate.advertise.util.Loggable
import akka.actor.actorRef2Scala
import akka.actor.Actor
import akka.actor.ActorRef
import akka.actor.Props
import akka.actor.ActorSystem

case class Invoke(f: Int => Unit, requestTime: Long)

class Async(name: String,system: ActorSystem) {
  
  val MAX_WORKER = 97
  
  val bgWorkers = for (i <- 0 until MAX_WORKER) yield {
    system.actorOf(Props(new AsyncWorker), name + "-" + i)
  }
  
  def asnyc(key: String)(f: => Unit) = {
    val ff = (x: Int) => f
    chooseWorker(key) ! Invoke(ff, System.currentTimeMillis)
  }
  
  
  private def chooseWorker(key: String): ActorRef = {
    val idx = (key.hashCode() & 0x7FFFFFFF) % MAX_WORKER
    bgWorkers(idx)
  }
}

class AsyncWorker extends Actor with Loggable {
  def receive = {
    case Invoke(f, requestTime) => {
      debug("delay async seconds: " + (System.currentTimeMillis - requestTime) / 1000d)
      f(0)
    }
  }
}
