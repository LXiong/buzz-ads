package com.buzzinate.advertise.algorithm

import com.buzzinate.advertise.elasticsearch.api.HitAd
import com.buzzinate.advertise.util.Loggable
import com.buzzinate.common.util.ip.ProvinceName
import com.buzzinate.buzzads.thrift.AdEntryTypeEnum

trait Recommend extends Loggable{
	def recommend(url: String, title: String, metaKeywords: List[String],  entryType: AdEntryTypeEnum, resourceSize: String, count: Int, userId: String, province: ProvinceName, netWork: Int, uuid: String): List[HitAd]
}