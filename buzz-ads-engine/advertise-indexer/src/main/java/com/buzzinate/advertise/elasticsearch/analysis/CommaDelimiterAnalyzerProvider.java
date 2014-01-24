package com.buzzinate.advertise.elasticsearch.analysis;

import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.inject.assistedinject.Assisted;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.index.Index;
import org.elasticsearch.index.analysis.AbstractIndexAnalyzerProvider;
import org.elasticsearch.index.settings.IndexSettings;

/**
 * 
 * @author feeling
 * 
 */
public class CommaDelimiterAnalyzerProvider extends AbstractIndexAnalyzerProvider<DelimiterAnalyzer> {

	private final DelimiterAnalyzer analyzer;
	private static final Character COMMA_DELIMITER = ',';

	@Inject
	public CommaDelimiterAnalyzerProvider(Index index, @IndexSettings Settings indexSettings, @Assisted String name, @Assisted Settings settings) {
		super(index, indexSettings, name, settings);
		this.analyzer = new DelimiterAnalyzer(version, COMMA_DELIMITER);
	}

	public DelimiterAnalyzer get() {
		return this.analyzer;
	}
}
