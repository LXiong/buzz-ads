package com.buzzinate.advertise.elasticsearch.query;

import java.io.IOException;
import java.util.Arrays;

import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.SingleTermsEnum;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.Terms;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.util.AttributeSource;

/**
 * 广告系统topic model Query
 * 
 * @author feeling
 *
 */
public class AdTopicQuery extends Query {

	protected String topicField;
	protected TopicInfo[] tis = null;

	/**
	 * Constructs a query matching terms that cannot be represented with a
	 * single Term.
	 */
	public AdTopicQuery(String indexField, TopicInfo[] tis) {
		this.topicField = indexField;
		this.tis = tis;
	}

	/**
	 * To rewrite to a simpler form, instead return a simpler enum from
	 * {@link #getTermsEnum(Terms, AttributeSource)}. For example, to rewrite to
	 * a single term, return a {@link SingleTermsEnum}
	 */
	@Override
	public final Query rewrite(IndexReader reader) throws IOException {

		BooleanQuery q = new BooleanQuery();
		for (TopicInfo ti : tis) {
			q.add(new AdTermQuery(new Term(topicField, ti.getTopic()), ti.getProbability()), Occur.SHOULD);
		}
		return q;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Float.floatToIntBits(getBoost());
		for (TopicInfo ti : tis)
			result = prime * result + ti.hashCode();
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AdTopicQuery other = (AdTopicQuery) obj;
		if (Float.floatToIntBits(getBoost()) != Float.floatToIntBits(other
				.getBoost()))
			return false;
		return Arrays.equals(tis, other.tis);
	}

	@Override
	public String toString(String field) {
		return "AdQuery[" + Arrays.toString(tis) + "]";
	}
}
