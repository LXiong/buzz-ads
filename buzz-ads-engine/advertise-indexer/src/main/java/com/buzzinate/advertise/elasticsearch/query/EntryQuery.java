package com.buzzinate.advertise.elasticsearch.query;

import java.io.IOException;

import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.SingleTermsEnum;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.TermQuery;

/**
 * 广告系统直接根据entryId进行查询的 Query
 * 
 * @author feeling
 *
 */
public class EntryQuery extends Query {

	protected String entryIdField;
	protected String[] entryIds;

	/**
	 * Constructs a query matching terms that cannot be represented with a
	 * single Term.
	 */
	public EntryQuery(String indexField, String[] entryIds) {
		this.entryIdField = indexField;
		this.entryIds = entryIds;
	}

	/**
	 * To rewrite to a simpler form, instead return a simpler enum from
	 * {@link #getTermsEnum(Terms, AttributeSource)}. For example, to rewrite to
	 * a single term, return a {@link SingleTermsEnum}
	 */
	@Override
	public final Query rewrite(IndexReader reader) throws IOException {

		BooleanQuery q = new BooleanQuery();
		for (String entryId : entryIds) {
			q.add(new TermQuery(new Term(entryIdField, entryId)), Occur.SHOULD);
		}
		return q;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Float.floatToIntBits(getBoost());
		for (String entryId : entryIds)
			result = prime * result + entryId.hashCode();
		return result;
	}

	@Override
	public String toString(String field) {
		StringBuilder sb = new StringBuilder();
		for(String entryId : entryIds) {
			sb.append(entryId).append(",");
		}
		return "EntryQuery[" + sb.toString() + "]";
	}
}
