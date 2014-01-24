package com.buzzinate.advertise.test
import com.buzzinate.advertise.server.Servers
import com.buzzinate.buzzads.event.AdEnableEvent

object TestEvent {
	def main(args: Array[String]): Unit = {
	  val enableEvent = new AdEnableEvent
	  enableEvent.setId(191)
	  Servers.kestrelClient.put("ad_event", enableEvent)
	}
}