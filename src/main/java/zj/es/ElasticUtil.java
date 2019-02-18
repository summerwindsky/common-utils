package zj.es;

import com.thunisoft.elasticsearch.ElasticReader;
import com.thunisoft.elasticsearch.ElasticWriter;
import com.thunisoft.elasticsearch.client.ElasticClient;
import com.thunisoft.icase.common.consts.Constants;
import org.apache.commons.lang.StringUtils;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Title: ElasticUtil
 * Description:
 *
 * @author tianxiupeng
 * @version 1.0
 * @date 2018年10月23日 下午1:17:14
 */
public class ElasticUtil {

	private static final Logger logger = LoggerFactory.getLogger(ElasticUtil.class);

	private static ElasticUtil instance = null;

	private static ElasticWriter esWriter = null;

	private static ElasticReader esReader = null;

	private ElasticUtil() {
		ElasticClient esClient = ElasticClient.getInstance(Constants.ES_CLUSTER, Constants.XPACK_USER_PASSWD,
				Constants.ES_IP, Constants.ES_PORT);
		esWriter = new ElasticWriter(esClient);
		esReader = new ElasticReader(esClient);
	}

	public static ElasticUtil getInstance() {
		if (null == instance) {
			synchronized (ElasticUtil.class) {
				if (null == instance) {
					instance = new ElasticUtil();
				}
			}
		}
		return instance;
	}

	public static void shutdown() {
		ElasticClient.shutdown();
	}

	public static ElasticClient getElasticClient() {
		return getInstance().esReader.getEsClient();
	}

	public Client getClient() {
		return getElasticClient().getClient();
	}

	public static ElasticWriter getElasticWriter() {
		return getInstance().esWriter;
	}

	public static ElasticReader getElasticReader() {
		return getInstance().esReader;
	}

	public static SearchSourceBuilder getSearchSourceBuilder(QueryBuilder query, String[] rFields) {
		SearchSourceBuilder searchBuilder = new SearchSourceBuilder();
		searchBuilder.query(query);
		String includes[] = {};
		if (null != rFields && rFields.length > 0) {
			includes = rFields;
		}
		String excludes[] = {};

		searchBuilder.fetchSource(includes, excludes);
		return searchBuilder;
	}

	/**
	 * 根据Id获取
	 *
	 * @param index
	 * @param type
	 * @param id
	 * @param rFields
	 * @return
	 */
	public static SearchResponse getResponseById(String index, String type, String id, String[] rFields) {
		SearchResponse response = null;
		TermQueryBuilder queryBuilder = QueryBuilders.termQuery("_id", id);
		SearchSourceBuilder searchBuilder = getSearchSourceBuilder(queryBuilder, rFields);
		SearchRequest searchRequest = getElasticReader().getSearchRequest(index, type);
		searchRequest.source(searchBuilder);
		response = getElasticReader().search(searchRequest);
		return response;
	}

	public static SearchResponse getResponse(String index, String type, QueryBuilder query, String[] rFields) {
		return getResponse(index, type, query, rFields, null);
	}

	public static SearchResponse getResponse(String index, String type, QueryBuilder query, String[] rFields,
	                                         String shard) {
		SearchResponse response = null;
		try {
			SearchSourceBuilder searchBuilder = getSearchSourceBuilder(query, rFields);

			searchBuilder.size(Constants.ES_SCROLL_SIZE);

			SearchRequest searchRequest = getElasticReader().getSearchRequest(index, type);
			searchRequest.source(searchBuilder);
			searchRequest.searchType(SearchType.QUERY_THEN_FETCH);
			searchRequest.scroll(new TimeValue(Constants.ES_SCROLL_TIMEOUT));
			if (StringUtils.isNotEmpty(shard)) {
				searchRequest.preference("_shards:" + shard);
			}
			response = getElasticReader().search(searchRequest);
		} catch (Exception e) {
			logger.error("index:" + index + "type" + type + "Cluster:" + Constants.ES_CLUSTER, e);
			return null;
		}
		return response;
	}

	/**
	 * 迭代取数据
	 *
	 * @param scrollId 迭代标识
	 * @return
	 */
	public static SearchResponse ScrollSearch(String scrollId) {
		if (null == scrollId) {
			logger.info("scrollId is null!");
			return null;
		}
		SearchResponse response = null;
		try {
			response = esReader.searchScroll(scrollId, Constants.ES_SCROLL_SIZE);

		} catch (Exception e) {
			logger.error("elasticsearch exception:{}", e);
			return null;
		}
		return response;
	}

}
