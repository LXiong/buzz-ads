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
import com.buzzinate.advertise.elasticsearch.query.EntryQuery;

/**
 * 解析广告entry Query原始text
 * 
 * @author feeling
 * 
 */
public class EntryQueryParser implements QueryParser {
	public static final String NAME = "ENTRY_QUERY_PARSER";

	public String[] names() {
		return new String[] { NAME, Strings.toCamelCase(NAME) };
	}

	public Query parse(QueryParseContext parseContext) throws IOException, QueryParsingException {
		XContentParser parser = parseContext.parser();
		String[] entryIds = null;
		String currentFieldName = null;
		XContentParser.Token token;
		while ((token = parser.nextToken()) != XContentParser.Token.END_OBJECT) {
			if (token == XContentParser.Token.FIELD_NAME) {
				currentFieldName = parser.currentName();
				token = parser.nextToken();
				if ("buzzadsQueryText".equals(currentFieldName)) {
					entryIds = parser.text().split(",");
				} else
					throw new BuzzAdsQueryParsingException(parseContext.index(), "[entry_query] query does not support [" + currentFieldName + "]");
			}
		}
		if (entryIds == null || entryIds.length == 0){
			throw new BuzzAdsQueryParsingException(parseContext.index(), "[entry_query] the entryIds should not be empty");
		}
		return new EntryQuery(FieldEnum.ENTRY_ID.getFieldName(), entryIds);
	}
}
