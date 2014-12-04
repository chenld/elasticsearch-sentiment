package zx.soft.es.web.resource;

import java.util.HashMap;

import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.QueryStringQueryBuilder.Operator;
import org.restlet.data.Form;
import org.restlet.data.Parameter;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import zx.soft.es.core.domain.CountResult;
import zx.soft.es.core.domain.SearchParameters;
import zx.soft.es.web.application.SearchApplication;
import zx.soft.utils.json.JsonUtils;

public class CountResource extends ServerResource {

	private SearchApplication application;
	private SearchParameters searchParameters;
	private Logger logger = LoggerFactory.getLogger(CountResource.class);

	@Override
	public void doInit() {
		application = (SearchApplication) getApplication();
		searchParameters = new SearchParameters();
		HashMap<String, String> params = new HashMap<>();
		Form form = getRequest().getResourceRef().getQueryAsForm();
		for (Parameter p : form) {
			if (p.getName() != null) {
				params.put(p.getName(), p.getValue());
			} else
				params.put(p.getName(), p.getValue());
		}
		searchParameters.setQ(params.get("q") == null ? "*" : params.get("q"));
		searchParameters.setDf(params.get("df") == null ? "_all" : params.get("df"));
		searchParameters.setAnalyzer(params.get("analyzer") == null ? "ik" : params.get("analyzer"));
		searchParameters.setDefault_operator(params.get("default_operator") == null ? Operator.AND : Operator
				.valueOf(params.get("default_operator")));
		searchParameters.setExplain(Boolean.valueOf(params.get("explain")) == null ? false : Boolean.valueOf(params
				.get("explain")));
		searchParameters.set_source(Boolean.valueOf(params.get("_source")) == null ? false : Boolean.valueOf(params
				.get("_source")));
		searchParameters.setFields(params.get("fields") == null ? "" : params.get("fields"));
		searchParameters.setSort(params.get("sort") == null ? "" : params.get("sort"));
		searchParameters.setTrack_scores(params.get("track_scores") == null ? true : Boolean.valueOf(params
				.get("track_scores")));
		searchParameters.setTimeout(params.get("timeout") == null ? new TimeValue(0) : new TimeValue(Long
				.getLong(params.get("timeout"))));
		searchParameters.setFrom(params.get("from") == null ? 0 : Integer.valueOf(params.get("from")));
		searchParameters.setSize(params.get("size") == null ? 10 : Integer.valueOf(params.get("size")));
		searchParameters.setSearch_type(params.get("search_type") == null ? "query_then_fetch" : params.get("exfield"));
		searchParameters.setLowercase_expanded_terms(params.get("lowercase_expanded_terms") == null ? true : Boolean
				.valueOf(params.get("lowercase_expanded_terms")));
		searchParameters.setAnalyze_wildcard(params.get("analyze_wildcard") == null ? true : Boolean.valueOf(params
				.get("analyze_wildcard")));
		searchParameters.setFq(params.get("fq") == null ? "*" : params.get("fq"));
		System.out.println(searchParameters.toString());

	}

	@Get("json")
	public Object getCountResult() {
		CountResult countResult = application.doCount(searchParameters);
		return JsonUtils.toJsonWithoutPretty(countResult);
	}

}
