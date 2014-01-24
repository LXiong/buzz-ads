package com.buzzinate.advertise.elasticsearch.analysis;

import java.io.IOException;
import java.io.Reader;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.core.WhitespaceTokenizer;
import org.apache.lucene.analysis.payloads.PayloadHelper;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.PayloadAttribute;
import org.apache.lucene.util.BytesRef;
import org.apache.lucene.util.Version;

/**
 * 将topic 分布概率量子化后，做为payload的参数传入，用来加强相应topic对应的权重。
 * 
 * @author feeling
 * 
 */
public class DistributionAnalyzer extends Analyzer {

	@Override
	protected TokenStreamComponents createComponents(String fieldName, Reader reader) {
		final Tokenizer source = new WhitespaceTokenizer(Version.LUCENE_43, reader);
		return new TokenStreamComponents(source, new FreqFieldPayloadTokenFilter(source, FreqFieldPayloadTokenFilter.DEFAULT_DELIMITER));
	}

	public static final class FreqFieldPayloadTokenFilter extends TokenFilter {
		public static final char DEFAULT_DELIMITER = '|';
		private static final Double QUANTUM_PRECISION = 100000.0;
		private final char delimiter;
		private final CharTermAttribute termAtt = addAttribute(CharTermAttribute.class);
		private final PayloadAttribute payAtt = addAttribute(PayloadAttribute.class);

		public FreqFieldPayloadTokenFilter(TokenStream input, char delimiter) {
			super(input);
			this.delimiter = delimiter;
		}

		@Override
		public boolean incrementToken() throws IOException {
			if (input.incrementToken()) {
				final char[] buffer = termAtt.buffer();
				final int length = termAtt.length();
				for (int i = 0; i < length; i++) {
					if (buffer[i] == delimiter) {
						int freq = getQuantumProb(Double.parseDouble((new String(buffer, i + 1, length - (i + 1)))));
						byte[] data = new byte[4];
						PayloadHelper.encodeInt(freq, data, 0);
						payAtt.setPayload(new BytesRef(data));
						termAtt.setLength(i); // simply set a new length
						return true;
					}
				}
				// we have not seen the delimiter
				payAtt.setPayload(null);
				return true;
			} else
				return false;
		}

		private int getQuantumProb(Double probability) {
			if (probability >= 0.0 && probability <= 1000.0) {
				Double dValue = (probability * QUANTUM_PRECISION);
				return dValue.intValue();
			} else {
				return 0;
			}

		}
	}
}
