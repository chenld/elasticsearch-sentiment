package zx.soft.es.delete;

import java.io.IOException;

import org.elasticsearch.action.deletebyquery.DeleteByQueryResponse;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

import zx.soft.es.client.BuildClient;
import zx.soft.es.model.SearchParameters;

public class Delete {

	public DeleteByQueryResponse deleteByQuery(SearchParameters searchParameters) throws IOException {
		DeleteByQueryResponse response = null;
		//防止误删所有记录
		if (searchParameters.getQ() != "*") {
			QueryBuilder query = QueryBuilders.queryString(searchParameters.getQ())
					.defaultField(searchParameters.getDf()).defaultOperator(searchParameters.getDefault_operator())
					.analyzer(searchParameters.getAnalyzer())
					.lowercaseExpandedTerms(searchParameters.isLowercase_expanded_terms())
					.analyzeWildcard(searchParameters.isAnalyze_wildcard());
			response = BuildClient.buildClient().prepareDeleteByQuery("spiderindextest").setQuery(query).execute()
					.actionGet();
		}
		return response;
	}

	public static void main(String[] args) {
		SearchParameters searchParameters = new SearchParameters();
		searchParameters.setQ(" 大家快乐");
		searchParameters.setTimeout(new TimeValue(1000000));
		//searchParameters.setSize(5);
		searchParameters.setFields("_source,content,nickname,read_count");
		searchParameters.setLowercase_expanded_terms(Boolean.FALSE);
		//searchParameters.setTrack_scores(false);
		//searchParameters.setFrom(1);
		//searchParameters.setExplain(true);
		//searchParameters.setDefault_operator(Operator.OR);
		//System.out.println(deleteByQuery(searchParameters).status());
	}
}
