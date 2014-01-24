package com.buzzinate.advertise.elasticsearch.exception;

import org.elasticsearch.index.Index;
import org.elasticsearch.index.query.QueryParsingException;

/**
 * 当传入的Query不符合标准时，抛出此异常
 * 
 * @author feeling
 * 
 */
public class BuzzAdsQueryParsingException extends QueryParsingException {

	public BuzzAdsQueryParsingException(Index index, String msg) {
		super(index, msg);
	}

}
