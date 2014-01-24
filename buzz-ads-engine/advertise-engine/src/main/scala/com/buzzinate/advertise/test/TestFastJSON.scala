package com.buzzinate.advertise.test
import com.buzzinate.advertise.redis.json.AdCtr
import com.buzzinate.advertise.redis.JEntry
import com.alibaba.fastjson.JSON

object TestFashJson {
	def main(args: Array[String]): Unit = {
	  val adctr = new AdCtr
	  adctr.setAdId("asdf")
	  adctr.setCtr(0.54)
	  adctr.setLastUpdateDay(9867l)
	   val e = new JEntry(adctr)
        val text = e.toJson
        
        val json = JSON.parseObject(text);
		val cacheTime = json.getLongValue("cacheTime");
		json.remove("cacheTime");
		val value = JSON.parseObject(json.toJSONString(), adctr.getClass());
		println(value.getAdId())
	}
}