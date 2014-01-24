package com.buzzinate.advertise.elasticsearch.api.enums;

import java.io.Serializable;

/**
 * the index field name enums
 * 
 * @author feeling
 *
 */
public enum FieldEnum implements Serializable{

	ENTRY_ID("entryId"),
	GROUP_ID("groupId"),  
	CAMPAIGN_ID("campaignId"),
	ADVERTISER_ID("advertiserId"),
	TITLE("title"),
	KEYWORD("keyword"),
	TOPIC_DISTRIBUTION("topicDistribution"),
	TOP_TOPIC_DISTRIBUTION("topTopicDistribution"),
	SCHEDULEDAY("scheduleDay"),
	START_TIME("startTime"),
	END_TIME("endTime"),
	START_SCHEDULETIME("startScheduleTime"),
	END_SCHEDULETIME("endScheduleTime"),
	LINK("link"),
	REALURL("realUrl"),
	RESOURCE_TYPE("resourceType"),
	RESOURCE_URL("resourceUrl"),
	RESOURCE_SIZE("resourceSize"),
	DISPLAY_URL("displayUrl"),
	DESCRIPTION("description"),
	NETWORK("network"),
	BID_TYPE("bidType"),
	AUDIENCE_MAIN_CAT("audienceMainCat"),
	AUDIENCE_SUB_CAT("audienceSubCat"),
	AUDIENCE_LEVEL_3_CAT("audienceLevel3Cat"),
	AD_MAIN_CAT("adMainCat"),
	AD_SUB_CAT("adSubCat"),
	AD_LEVEL_3_CAT("adLevel3Cat"),
	BID_PRICE("bidPrice"),
	STATUS("status"),
	LOCATION("location"),
	CHANNEL("channel"),
	LASTMODIFIED("lastModified");
	
	private final String fieldName;
	
	private FieldEnum (String fieldName) {
        this.fieldName = fieldName;
    }
	
	public String getFieldName() {
		return fieldName;
	}
	
}
