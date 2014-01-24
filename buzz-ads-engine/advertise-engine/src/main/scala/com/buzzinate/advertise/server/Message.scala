package com.buzzinate.advertise.server

import com.buzzinate.buzzads.data.thrift.TAdCriteria
import com.buzzinate.buzzads.data.thrift.TAdStatsCriteria

object Message {
  
  case class CheckIndex(criteria: TAdCriteria, monitMode: Boolean, initMode: Boolean)
  case class CheckUpdateField(id: String, fieldName: String, fieldValue: Object)
  case class CheckAdStats(criteria: TAdStatsCriteria, monitMode: Boolean)
  case class CheckFrozenChannel(monitMode: Boolean)
  case class CheckEvent
  
  case class UserCategoryLog(isHit: Boolean, isTimeout: Boolean, cost: Long = -1)
  
  case class HeartBeat
  
  abstract class AdLevel
  
  case class CampaignLevel extends AdLevel {
    override def toString(): String = {
      "CampaignLevel:"
    }
  }
  case class OrderLevel extends AdLevel {
    override def toString(): String = {
      "OrderLevel:"
    }
  }
  case class EntryLevel extends AdLevel {
    override def toString(): String = {
      "EntryLevel:"
    }
  }

}