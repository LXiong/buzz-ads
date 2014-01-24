package com.buzzinate.advertise.elasticsearch.exception;

/**
 * the base Exception of BuzzAdsIndexer Service
 * 
 * @author feeling
 * 
 */
public class BuzzAdsIndexerException extends RuntimeException {

	static final long serialVersionUID = -7034897190745966939L;

	public BuzzAdsIndexerException(String msg) {
		super(msg);
	}

	public BuzzAdsIndexerException(String msg, Throwable cause) {
		super(msg, cause);
	}
}
