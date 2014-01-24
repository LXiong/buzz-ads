package com.buzzinate.advertise.elasticsearch.plugin;

import org.elasticsearch.index.analysis.AnalysisModule;
import org.elasticsearch.index.query.IndexQueryParserModule;
import org.elasticsearch.plugins.AbstractPlugin;

import com.buzzinate.advertise.elasticsearch.analysis.CommaDelimiterAnalyzerProvider;
import com.buzzinate.advertise.elasticsearch.analysis.DistributionAnalyzerProvider;
import com.buzzinate.advertise.elasticsearch.parser.AdQueryParser;
import com.buzzinate.advertise.elasticsearch.parser.EntryQueryParser;

/**
 * 广告搜索平台的Elastic Search插件
 * 
 * @author feeling
 * 
 */
public class AdvertisePlugin extends AbstractPlugin {

	public String name() {
		return "advertisement-plugin";
	}

	public String description() {
		return "Buzz-ads recommendation plugin";
	}

	public void onModule(AnalysisModule module) {
		module.addAnalyzer("comma_analyzer", CommaDelimiterAnalyzerProvider.class);
		module.addAnalyzer("distribution_analyzer", DistributionAnalyzerProvider.class);
	}

	public void onModule(IndexQueryParserModule module) {
		module.addQueryParser(AdQueryParser.NAME, AdQueryParser.class);
		module.addQueryParser(EntryQueryParser.NAME, EntryQueryParser.class);
	}
}
