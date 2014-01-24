package com.buzzinate.advertise.elasticsearch.query;

import com.buzzinate.advertise.util.StringUtils;

/**
 * 每条topic的原子信息
 * 
 * @author feeling
 * 
 */
public class TopicInfo {
	private String topic;
	private Double probability;

	public TopicInfo(String topic, Double probability) {
		this.topic = topic;
		this.probability = probability;
	}

	public String getTopic() {
		return topic;
	}

	public Double getProbability() {
		return probability;
	}

	/**
	 * parse the topicDistribution: "1|0.8 38|0.2" , the probability of topic 1
	 * is 0.8, topic 2 is 0.2
	 * 
	 * @param text
	 * @return
	 */
	public static TopicInfo[] parse(String text) {
		String[] parts = StringUtils.split(text, ' ');
		TopicInfo[] tis = new TopicInfo[parts.length];
		for (int i = 0; i < tis.length; i++) {
			String[] ti = StringUtils.split(parts[i], '|');
			String term = ti[0];
			Double probability = Double.parseDouble(ti[1]);
			tis[i] = new TopicInfo(term, probability);
		}
		return tis;
	}

	@Override
	public String toString() {
		return topic + "(probability=" + probability + ")";
	}

}
