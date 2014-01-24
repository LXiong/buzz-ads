package com.buzzinate.advertise.corpus.crawl

trait AdsLinkCollector {
	def getAdsLinks(): Set[String]
}