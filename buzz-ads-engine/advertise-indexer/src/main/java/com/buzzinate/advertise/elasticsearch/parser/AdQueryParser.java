package com.buzzinate.advertise.elasticsearch.parser;

import java.io.IOException;

import org.apache.lucene.search.Query;
import org.elasticsearch.common.Strings;
import org.elasticsearch.common.xcontent.XContentParser;
import org.elasticsearch.index.query.QueryParseContext;
import org.elasticsearch.index.query.QueryParser;
import org.elasticsearch.index.query.QueryParsingException;

import com.buzzinate.advertise.elasticsearch.api.enums.FieldEnum;
import com.buzzinate.advertise.elasticsearch.exception.BuzzAdsQueryParsingException;
import com.buzzinate.advertise.elasticsearch.query.AdTopicQuery;
import com.buzzinate.advertise.elasticsearch.query.TopicInfo;

/**
 * 解析广告topic Query原始text
 * 
 * @author feeling
 * 
 */
public class AdQueryParser implements QueryParser {
	public static final String NAME = "AD_QUERY_PARSER";

	public String[] names() {
		return new String[] { NAME, Strings.toCamelCase(NAME) };
	}

	public Query parse(QueryParseContext parseContext) throws IOException, QueryParsingException {
		XContentParser parser = parseContext.parser();

		TopicInfo[] tis = null;

		String currentFieldName = null;
		XContentParser.Token token;
		while ((token = parser.nextToken()) != XContentParser.Token.END_OBJECT) {
			if (token == XContentParser.Token.FIELD_NAME) {
				currentFieldName = parser.currentName();
				token = parser.nextToken();
				if ("buzzadsQueryText".equals(currentFieldName)) {
					try {
						tis = TopicInfo.parse(parser.text());
					} catch (Exception e) {
						throw new BuzzAdsQueryParsingException(parseContext.index(), "the topicDistribution is illgal : " + parser.text());
					}
				} else
					throw new BuzzAdsQueryParsingException(parseContext.index(), "[ad_query] query does not support [" + currentFieldName + "]");
			}
		}
		return new AdTopicQuery(FieldEnum.TOPIC_DISTRIBUTION.getFieldName(), tis);
	}

}
