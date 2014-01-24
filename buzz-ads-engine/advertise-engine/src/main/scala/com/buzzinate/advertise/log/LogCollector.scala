package com.buzzinate.advertise.log

import akka.actor.Actor
import com.buzzinate.advertise.util.Loggable
import com.buzzinate.advertise.server.Message.UserCategoryLog
import scala.collection.mutable.ListBuffer

class LogCollector extends Actor with Loggable {

  val userCategoryLogBuffer = new ListBuffer[UserCategoryLog]

  def receive = {

    case ucLog: UserCategoryLog => {
      userCategoryLogBuffer.append(ucLog)
      checkFlush
    }

  }

  def checkFlush {
    if (userCategoryLogBuffer.size > 10000) {
      var hitTimes = 0
      var timeoutTimes = 0
      var totalCost = 0l
      userCategoryLogBuffer.foreach { ucLog =>
        if (ucLog.isHit) {
          hitTimes += 1
        }
        if (ucLog.isTimeout) {
          timeoutTimes += 1
        } else {
          totalCost += ucLog.cost 
        }
      }
      val sb = new StringBuilder
      sb.append("[LogCollector]:userCategoryLog; average userCategory hit rate => " + hitTimes.toDouble / userCategoryLogBuffer.size)//.append(hitTimes.toDouble / userCategoryLogBuffer.size)
      sb.append(" timeout rate => " + timeoutTimes.toDouble / userCategoryLogBuffer.size)
      //防止除数为0
      sb.append(" average cost => " + totalCost.toDouble / (userCategoryLogBuffer.size + 1 - timeoutTimes) + " ms")
      info(sb.toString)
      userCategoryLogBuffer.clear
    }
  }
}