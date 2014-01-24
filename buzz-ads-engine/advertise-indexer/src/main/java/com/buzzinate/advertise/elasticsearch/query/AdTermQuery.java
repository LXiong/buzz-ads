package com.buzzinate.advertise.elasticsearch.query;

import java.io.IOException;
import java.util.Set;

import org.apache.lucene.index.AtomicReaderContext;
import org.apache.lucene.index.DocsAndPositionsEnum;
import org.apache.lucene.index.IndexReaderContext;
import org.apache.lucene.index.ReaderUtil;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.TermContext;
import org.apache.lucene.index.TermState;
import org.apache.lucene.index.TermsEnum;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.ComplexExplanation;
import org.apache.lucene.search.Explanation;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.Scorer;
import org.apache.lucene.search.Weight;
import org.apache.lucene.search.similarities.Similarity;
import org.apache.lucene.search.similarities.Similarity.ExactSimScorer;
import org.apache.lucene.util.Bits;
import org.apache.lucene.util.ToStringUtils;

/**
 * A Query that matches documents containing a term. This may be combined with
 * other terms with a {@link BooleanQuery}.
 * 
 * @author feeling
 */
public class AdTermQuery extends Query {
	private final Term term;

	private final int docFreq;
	private final Double weight;
	private final TermContext perReaderTermState;

	final class TermWeight extends Weight {
		private final Similarity similarity;
		private final Similarity.SimWeight stats;
		private final TermContext termStates;

		public TermWeight(IndexSearcher searcher, TermContext termStates) throws IOException {
			assert termStates != null : "TermContext must not be null";
			this.termStates = termStates;
			this.similarity = searcher.getSimilarity();
			this.stats = similarity.computeWeight(getBoost(), searcher.collectionStatistics(term.field()), searcher.termStatistics(term, termStates));
		}

		@Override
		public String toString() {
			return "weight(" + AdTermQuery.this + ")";
		}

		@Override
		public Query getQuery() {
			return AdTermQuery.this;
		}

		@Override
		public float getValueForNormalization() {
			return stats.getValueForNormalization() * weight.floatValue();
		}

		@Override
		public void normalize(float queryNorm, float topLevelBoost) {
			stats.normalize(queryNorm, topLevelBoost);
		}

		@Override
		public Scorer scorer(AtomicReaderContext context, boolean scoreDocsInOrder, boolean topScorer, Bits acceptDocs) throws IOException {
			assert termStates.topReaderContext == ReaderUtil.getTopLevelContext(context) : "The top-reader used to create Weight ("
					+ termStates.topReaderContext + ") is not the same as the current reader's top-reader (" + ReaderUtil.getTopLevelContext(context);

			final TermState state = termStates.get(context.ord);
			if (state == null)
				return null;

			final TermsEnum termsEnum = context.reader().terms(term.field()).iterator(null);
			termsEnum.seekExact(term.bytes(), state);
			final DocsAndPositionsEnum postings = termsEnum.docsAndPositions(acceptDocs, null, DocsAndPositionsEnum.FLAG_PAYLOADS);

			assert postings != null;
			return new AdTermScorer(this, postings, similarity.exactSimScorer(stats, context), termsEnum.docFreq(), weight);
		}

		@Override
		public Explanation explain(AtomicReaderContext context, int doc) throws IOException {
			Scorer scorer = scorer(context, true, false, context.reader().getLiveDocs());
			if (scorer != null) {
				int newDoc = scorer.advance(doc);
				if (newDoc == doc) {
					float freq = scorer.freq();
					ExactSimScorer docScorer = similarity.exactSimScorer(stats, context);
					ComplexExplanation result = new ComplexExplanation();
					result.setDescription("weight(" + getQuery() + " in " + doc + ") [" + similarity.getClass().getSimpleName() + "], result of:");
					Explanation scoreExplanation = docScorer.explain(doc, new Explanation(freq, "termFreq=" + freq));
					result.addDetail(scoreExplanation);
					result.setValue(scoreExplanation.getValue());
					result.setMatch(true);
					return result;
				}
			}
			return new ComplexExplanation(false, 0.0f, "no matching term");
		}
	}

	public AdTermQuery(Term term) {
		this(term, 1.0);
	}

	public AdTermQuery(Term term, Double weight) {
		this(term, weight, -1);

	}

	public AdTermQuery(Term term, Double weight, int docFreq) {
		this.term = term;
		this.weight = weight;
		this.docFreq = docFreq;
		this.perReaderTermState = null;
	}

	/** Returns the term of this query. */
	public Term getTerm() {
		return term;
	}

	@Override
	public Weight createWeight(IndexSearcher searcher) throws IOException {
		final IndexReaderContext context = searcher.getTopReaderContext();
		final TermContext termState;
		if (perReaderTermState == null || perReaderTermState.topReaderContext != context) {
			// make TermQuery single-pass if we don't have a PRTS or if the
			// context differs!
			termState = TermContext.build(context, term, true); // cache term
																// lookups!
		} else {
			// PRTS was pre-build for this IS
			termState = this.perReaderTermState;
		}

		// we must not ignore the given docFreq - if set use the given value
		// (lie)
		if (docFreq != -1)
			termState.setDocFreq(docFreq);

		return new TermWeight(searcher, termState);
	}

	@Override
	public void extractTerms(Set<Term> terms) {
		terms.add(getTerm());
	}

	/** Prints a user-readable version of this query. */
	@Override
	public String toString(String field) {
		StringBuilder buffer = new StringBuilder();
		if (!term.field().equals(field)) {
			buffer.append(term.field());
			buffer.append(":");
		}
		buffer.append(term.text());
		buffer.append(ToStringUtils.boost(getBoost()));
		return buffer.toString();
	}

	/** Returns true iff <code>o</code> is equal to this. */
	@Override
	public boolean equals(Object o) {
		if (!(o instanceof AdTermQuery))
			return false;
		AdTermQuery other = (AdTermQuery) o;
		return (this.getBoost() == other.getBoost()) && this.term.equals(other.term);
	}

	/** Returns a hash code value for this object. */
	@Override
	public int hashCode() {
		return Float.floatToIntBits(getBoost()) ^ term.hashCode();
	}

}
