package com.buzzinate.advertise.elasticsearch.analysis;

import java.io.Reader;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.util.CharTokenizer;
import org.apache.lucene.util.Version;

/**
 * An Analyzer that uses {@link DelimiterTokenizer}.
 * <p>
 * <a name="version">You must specify the required {@link Version} compatibility
 * when creating {@link CharTokenizer}:
 * <ul>
 * <li>As of 3.1, {@link DelimiterTokenizer} uses an int based API to normalize
 * and detect token codepoints. See {@link CharTokenizer#isTokenChar(int)} and
 * {@link CharTokenizer#normalize(int)} for details.</li>
 * </ul>
 * <p>
 **/
public final class DelimiterAnalyzer extends Analyzer {

	private final Version matchVersion;
	private final Character delimiter;

	/**
	 * Creates a new {@link DelimiterAnalyzer}
	 * 
	 * @param matchVersion
	 *            Lucene version to match See
	 *            {@link <a href="#version">above</a>}
	 */
	public DelimiterAnalyzer(Version matchVersion, Character delimiter) {
		this.delimiter = delimiter;
		this.matchVersion = matchVersion;
	}

	@Override
	protected TokenStreamComponents createComponents(final String fieldName, final Reader reader) {
		return new TokenStreamComponents(new DelimiterTokenizer(matchVersion, reader, delimiter));
	}
}
