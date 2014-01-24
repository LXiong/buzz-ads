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
public class DistributionAnalyzerProvider extends AbstractIndexAnalyzerProvider<DistributionAnalyzer> {

	private final DistributionAnalyzer analyzer;

	@Inject
	public DistributionAnalyzerProvider(Index index, @IndexSettings Settings indexSettings, @Assisted String name, @Assisted Settings settings) {
		super(index, indexSettings, name, settings);
		analyzer = new DistributionAnalyzer();
	}

	public DistributionAnalyzer get() {
		return this.analyzer;
	}
}
