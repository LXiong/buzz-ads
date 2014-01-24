package com.buzzinate.advertise.elasticsearch.exception;

/**
 * 当插入不符合标准的广告时(例如)，抛出此异常。
 * 
 * @author feeling
 * 
 */
public class BuzzAdsIndexerIllegalAdException extends BuzzAdsIndexerException {

	public BuzzAdsIndexerIllegalAdException() {
		super(null);
	}

	public BuzzAdsIndexerIllegalAdException(String msg) {
		super(msg);
	}

	public BuzzAdsIndexerIllegalAdException(String msg, Throwable cause) {
		super(msg, cause);
	}
}
