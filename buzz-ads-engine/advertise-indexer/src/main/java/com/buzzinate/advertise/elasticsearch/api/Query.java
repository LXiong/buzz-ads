package com.buzzinate.advertise.elasticsearch.api;

/**
 * 广告平台的query，text类型，目前有两种格式“
 * 1. 一个topicDistribution text。 Example： "0|0.7 45|0.23 99|0.07
 * 2. 一个entryIds text。Example: "5,23,3,543"
 * @author feeling
 * 
 */
public class Query {

	public String buzzadsQueryText;

	public Query() {

	}

	public Query(String buzzadsQueryText) {
		this.buzzadsQueryText = buzzadsQueryText;
	}
}
