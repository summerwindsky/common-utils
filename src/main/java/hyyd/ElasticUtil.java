package hyyd;

import org.apache.commons.lang.StringUtils;
import org.elasticsearch.action.index.IndexResponse;
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

import com.thunisoft.elasticsearch.ElasticReader;
import com.thunisoft.elasticsearch.ElasticWriter;
import com.thunisoft.elasticsearch.client.ElasticClient;
import com.thunisoft.hyyd.system.config.Constant;

/**
 * 
 * Title: ElasticUtil
 * Description: 
 * Company: 北京华宇元典信息服务有限公司
 *
 * @author tianxiupeng
 * @version 1.0
 * @date 2018年10月23日 下午1:17:14
 *
 */
public class ElasticUtil {

    private static final Logger logger = LoggerFactory.getLogger(ElasticUtil.class);

    private static ElasticUtil instance = null;

    private ElasticWriter esWriter = null;

    private ElasticReader esReader = null;

    private ElasticUtil() {
        ElasticClient esClient = ElasticClient.getInstance(Constant.es_cluster_name, Constant.es_xpack_auth,
            Constant.es_cluster_ip, Constant.es_cluster_port);
        esWriter = new ElasticWriter(esClient);
        esReader = new ElasticReader(esClient);
    }

    public synchronized static ElasticUtil getInstance() {
        if (null == instance) {
            instance = new ElasticUtil();
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
        searchBuilder.size(10000);
        String abc[] = {};
        searchBuilder.fetchSource(rFields, abc);
        return searchBuilder;
    }

    /**
     * 根据Id获取
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

    /**
     * 根据Id获取
     * @param index
     * @param type
     * @param rFields
     * @return
     */
    public static SearchResponse getResponseByTerm(String index, String type,String termName, String termValue, String[] rFields) {
        SearchResponse response = null;
        TermQueryBuilder queryBuilder = QueryBuilders.termQuery(termName, termValue);
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

            searchBuilder.size(Constant.es_scroll_size);

            SearchRequest searchRequest = getElasticReader().getSearchRequest(index, type);
            searchRequest.source(searchBuilder);
            searchRequest.searchType(SearchType.QUERY_THEN_FETCH);
            searchRequest.scroll(new TimeValue(Constant.es_scroll_timeout));
            if (StringUtils.isNotEmpty(shard)) {
                searchRequest.preference("_shards:" + shard);
            }
            response = getElasticReader().search(searchRequest);
            //            logger.info("es request : {}", searchRequest.toString());
        } catch (Exception e) {
            logger.error("index:" + index + "type" + type + "Cluster:" + Constant.es_cluster_name, e);
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
    public SearchResponse ScrollSearch(String scrollId) {
        if (null == scrollId) {
            logger.info("scrollId is null!");
            return null;
        }
        SearchResponse response = null;
        try {
            response = esReader.searchScroll(scrollId, Constant.es_scroll_size);

        } catch (Exception e) {
            logger.error("elasticsearch exception:{}", e);
            return null;
        }
        return response;
    }

    public static void index(String index, String type,String id, Object source) {
        index(index, type, null, id, source, false);
    }

    public static void index(String index, String type, String pid, String id, Object source, boolean refresh) {
        IndexResponse r = getElasticWriter().index(index, type, pid, id, source, refresh);
    }

}
