package com.buzzinate.advertise.elasticsearch.analysis;

import java.io.Reader;

import org.apache.lucene.analysis.util.CharTokenizer;
import org.apache.lucene.util.Version;

/**
 * 
 * @author feeling
 * 
 */
public final class DelimiterTokenizer extends CharTokenizer {

	private Character delimiter;

	public DelimiterTokenizer(Version matchVersion, Reader in, Character delimiter) {
		super(matchVersion, in);
		this.delimiter = delimiter;
	}

	public DelimiterTokenizer(Version matchVersion, AttributeFactory factory, Reader in, Character delimiter) {
		super(matchVersion, factory, in);
		this.delimiter = delimiter;
	}

	@Override
	protected boolean isTokenChar(int c) {
		return !(delimiter.charValue() == c);
	}
}
