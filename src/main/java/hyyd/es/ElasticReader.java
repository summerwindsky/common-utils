package hyyd.es;

import com.thunisoft.elasticsearch.client.ElasticClient;
import org.apache.commons.lang.StringUtils;
import org.elasticsearch.action.admin.cluster.shards.ClusterSearchShardsGroup;
import org.elasticsearch.action.admin.cluster.shards.ClusterSearchShardsRequest;
import org.elasticsearch.action.admin.cluster.shards.ClusterSearchShardsResponse;
import org.elasticsearch.action.search.*;
import org.elasticsearch.client.Requests;
import org.elasticsearch.cluster.node.DiscoveryNode;
import org.elasticsearch.cluster.routing.ShardRouting;
import org.elasticsearch.common.Strings;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.common.xcontent.XContentParser;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryParseContext;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author gaojun
 * @Title ElesticReader
 * @Description es操作类-检索
 * @date 2015-8-12 下午5:37:28
 * @Company 北京华宇信息技术有限公司
 */

public class ElasticReader {

    private static final Logger logger = LoggerFactory.getLogger(ElasticReader.class);

    private ElasticClient esClient = null;

    public ElasticReader(String clusterName, String xpackSecurityUser, String ip, int port) {
        esClient = ElasticClient.getInstance(clusterName, xpackSecurityUser, ip, port);
    }

    public ElasticReader(String clusterName, String ip, int port) {
        esClient = ElasticClient.getInstance(clusterName, null, ip, port);
    }

    public ElasticReader(String clusterName, String xpackSecurityUser, String ip) {
        esClient = ElasticClient.getInstance(clusterName, xpackSecurityUser, ip);
    }

    public ElasticReader(String clusterName, String ip) {
        esClient = ElasticClient.getInstance(clusterName, null, ip);
    }

    public ElasticReader(ElasticClient esClient) {
        this.esClient = esClient;
    }

    /**
     * 获取shard信息
     *
     * @param index
     * @param type
     * @return
     */
    public Map<String, DiscoveryNode> getShards(String index, String type) {
        final ClusterSearchShardsRequest clusterSearchShardsRequest = Requests
                .clusterSearchShardsRequest(Strings.splitStringByCommaToArray(index));
        ClusterSearchShardsResponse response = esClient.getClient().admin().cluster()
                .searchShards(clusterSearchShardsRequest).actionGet();

        DiscoveryNode[] nodes = response.getNodes();

        Map<String, DiscoveryNode> shardsToNode = new HashMap<String, DiscoveryNode>();
        Map<String, DiscoveryNode> nodesMap = new HashMap<String, DiscoveryNode>();
        for (DiscoveryNode node : nodes) {
            nodesMap.put(node.getId(), node);
        }

        ClusterSearchShardsGroup[] shardsGroup = response.getGroups();

        for (ClusterSearchShardsGroup group : shardsGroup) {
            int shardId = group.getShardId().getId();

            for (ShardRouting shard : group.getShards()) {
                if (shard.primary() && shard.started()) {
                    String nodeId = shard.currentNodeId();
                    DiscoveryNode node = nodesMap.get(nodeId);
                    shardsToNode.put("" + shardId, node);
                }
            }
        }

        return shardsToNode;
    }

    public SearchSourceBuilder parseSearchSourceBuilder(String searchSource) throws IOException {
        XContentType xContentType = XContentFactory.xContentType(searchSource);
        return parseSearchSourceBuilder(searchSource, xContentType);
    }

    public SearchSourceBuilder parseSearchSourceBuilder(String searchSource, XContentType xContentType) throws IOException {
        if (StringUtils.isBlank(searchSource) || !XContentType.JSON.equals(xContentType)) {
            logger.warn("searchSource is blank or searchSource is not JSON");
            return null;
        }
        SearchSourceBuilder ssb = SearchSourceBuilder.searchSource();
        XContentParser xParser = xContentType.xContent().createParser(esClient.getxContentRegistry(), searchSource);
        QueryParseContext qpc = new QueryParseContext(xParser);
        ssb.parseXContent(qpc);

        return ssb;
    }

    /******************************************* scroll ***************************************************************/
    /**
     * 迭代遍历数据
     *
     * @param scrollId
     * @param es_scroll_timeout
     * @return
     */
    public SearchResponse searchScroll(String scrollId, long es_scroll_timeout) {
        SearchScrollRequestBuilder builder = esClient.getClient().prepareSearchScroll(scrollId);
        builder.setScroll(new TimeValue(es_scroll_timeout));

        return builder.execute().actionGet();
    }

    public SearchResponse prepareScroll(String[] indices, String[] types, String searchsource, long es_scroll_timeout) throws IOException {
        SearchSourceBuilder ssb = parseSearchSourceBuilder(searchsource);
        return prepareScroll(indices, types, ssb, es_scroll_timeout);
    }

    public SearchResponse prepareScroll(String[] indices, String[] types, SearchSourceBuilder ssb, long es_scroll_timeout) {
        return esClient.getClient().prepareSearch(indices).setTypes(indices).setSource(ssb)
                .setScroll(new TimeValue(es_scroll_timeout)).addSort(FieldSortBuilder.DOC_FIELD_NAME, SortOrder.ASC)
                .execute().actionGet();
    }

    /**
     * 获取ClearScrollRequest
     *
     * @param cursorIds scrollId列表，支持多scrollId，用英文逗号(,)分隔
     * @return
     */
    public ClearScrollRequest getClearScrollRequest(String cursorIds) {
        return getClearScrollRequestBuilder(cursorIds).request();
    }

    /**
     * 获取ClearScrollRequest
     *
     * @param cursorIds
     * @return
     */
    public ClearScrollRequest getClearScrollRequest(Set<String> cursorIds) {
        return getClearScrollRequestBuilder(cursorIds).request();
    }

    /**
     * 获取ClearScrollRequestBuilder
     *
     * @param cursorIds scrollId列表，支持多scrollId，用英文逗号(,)分隔
     * @return
     */
    public ClearScrollRequestBuilder getClearScrollRequestBuilder(String cursorIds) {
        Set<String> set = Strings.splitStringByCommaToSet(cursorIds);
        return getClearScrollRequestBuilder(set);
    }

    /**
     * 获取ClearScrollRequestBuilder
     *
     * @param cursorIds
     * @return
     */
    public ClearScrollRequestBuilder getClearScrollRequestBuilder(Set<String> cursorIds) {
        ClearScrollRequestBuilder builder = esClient.getClient().prepareClearScroll();
        for (String sId : cursorIds) {
            if (StringUtils.isNotBlank(sId)) {
                builder.addScrollId(sId);
            }
        }
        return builder;
    }

    /**
     * 执行clearScroll
     *
     * @param cursorIds scrollId列表，支持多scrollId，用英文逗号(,)分隔
     * @return
     */
    public ClearScrollResponse clearScroll(String cursorIds) {
        ClearScrollRequest request = getClearScrollRequest(cursorIds);
        return clearScroll(request);
    }

    /**
     * 执行clearScroll
     *
     * @param cursorIds
     * @return
     */
    public ClearScrollResponse clearScroll(Set<String> cursorIds) {
        ClearScrollRequest request = getClearScrollRequest(cursorIds);
        return clearScroll(request);
    }

    /**
     * 执行clearScroll
     *
     * @param request
     * @return
     */
    public ClearScrollResponse clearScroll(ClearScrollRequest request) {
        return esClient.getClient().clearScroll(request).actionGet();
    }

    /******************************************* count request ***************************************************************/
    /**
     * 获取countRequest
     *
     * @param indices
     * @param types
     * @return
     */
    public SearchRequest getCountRequest(String[] indices, String[] types) {
        return getCountRequestBuilder(indices, types).request();
    }

    /**
     * 获取countRequest
     *
     * @param indices
     * @return
     */
    public SearchRequest getCountRequest(String[] indices) {
        return getCountRequestBuilder(indices).request();
    }

    /**
     * 获取countRequest
     *
     * @param index 索引名称，支持多索引名称，用英文逗号(,)分隔
     * @param type  type名称，支持多type名称，用英文逗号(,)分隔
     * @return
     */
    public SearchRequest getCountRequest(String index, String type) {
        return getCountRequestBuilder(index, type).request();
    }

    /**
     * 获取countRequest
     *
     * @param index 索引名称，支持多索引名称，用英文逗号(,)分隔
     * @return
     */
    public SearchRequest getCountRequest(String index) {
        return getCountRequestBuilder(index).request();
    }

    /******************************************* countRequestBuilder ***************************************************************/
    /**
     * 获取CountRequestBuilder
     *
     * @param indices
     * @param types
     * @return
     */
    public SearchRequestBuilder getCountRequestBuilder(String[] indices, String[] types) {
        if (types == null) {
            types = Strings.EMPTY_ARRAY;
        }
        return getCountRequestBuilder(indices).setTypes(types);
    }

    /**
     * 获取CountRequestBuilder
     *
     * @param indices
     * @param types
     * @return
     */
    public SearchRequestBuilder getCountRequestBuilder(String[] indices) {
        if (indices == null) {
            indices = Strings.EMPTY_ARRAY;
        }
        return esClient.getClient().prepareSearch(indices).setSize(0);
    }

    /**
     * 获取CountRequestBuilder
     *
     * @param index 索引名称，支持多索引名称，用英文逗号(,)分隔
     * @param type  type名称，支持多type名称，用英文逗号(,)分隔
     * @return
     */
    public SearchRequestBuilder getCountRequestBuilder(String index, String type) {
        String[] indices = Strings.splitStringByCommaToArray(index);
        String[] types = Strings.splitStringByCommaToArray(type);
        return getCountRequestBuilder(indices, types);
    }

    /**
     * 获取CountRequestBuilder
     *
     * @param index 索引名称，支持多索引名称，用英文逗号(,)分隔
     * @return
     */
    public SearchRequestBuilder getCountRequestBuilder(String index) {
        String[] indices = Strings.splitStringByCommaToArray(index);
        return getCountRequestBuilder(indices);
    }

    /******************************************* search request ***************************************************************/
    /**
     * 获取SearchRequest
     *
     * @param indices
     * @param types
     * @return
     */
    public SearchRequest getSearchRequest(String[] indices, String[] types) {
        return getSearchRequestBuilder(indices, types).request();
    }

    /**
     * 获取SearchRequest
     *
     * @param index 索引名称，支持多索引名称，用英文逗号(,)分隔
     * @param type  type名称，支持多type名称，用英文逗号(,)分隔
     * @return
     */
    public SearchRequest getSearchRequest(String index, String type) {
        return getSearchRequestBuilder(index, type).request();
    }

    /**
     * 获取SearchRequest
     *
     * @param index 索引名称，支持多索引名称，用英文逗号(,)分隔
     * @return
     */
    public SearchRequest getSearchRequest(String index) {
        return getSearchRequestBuilder(index).request();
    }

    /**
     * 获取SearchRequest
     *
     * @param indices
     * @return
     */
    public SearchRequest getSearchRequest(String[] indices) {
        return getSearchRequestBuilder(indices).request();
    }

    /******************************************* searchRequestBuilder ***************************************************************/
    /**
     * 获取SearchRequestBuilder
     *
     * @param index 索引名称，支持多索引名称，用英文逗号(,)分隔
     * @param type  type名称，支持多type名称，用英文逗号(,)分隔
     * @return
     */
    public SearchRequestBuilder getSearchRequestBuilder(String index, String type) {
        String[] indices = Strings.splitStringByCommaToArray(index);
        String[] types = Strings.splitStringByCommaToArray(type);
        return getSearchRequestBuilder(indices, types);
    }

    /**
     * 获取SearchRequestBuilder
     *
     * @param indices
     * @param types
     * @return
     */
    public SearchRequestBuilder getSearchRequestBuilder(String[] indices, String[] types) {
        if (types == null) {
            types = Strings.EMPTY_ARRAY;
        }
        return getSearchRequestBuilder(indices).setTypes(types);
    }

    /**
     * 获取SearchRequestBuilder
     *
     * @param indices index 索引名称，支持多索引名称，用英文逗号(,)分隔
     * @return
     */
    public SearchRequestBuilder getSearchRequestBuilder(String[] indices) {
        if (indices == null) {
            indices = Strings.EMPTY_ARRAY;
        }
        return esClient.getClient().prepareSearch(indices);
    }

    /**
     * 获取SearchRequestBuilder
     *
     * @param index
     * @return
     */
    public SearchRequestBuilder getSearchRequestBuilder(String index) {
        String[] indices = Strings.splitStringByCommaToArray(index);
        return getSearchRequestBuilder(indices);
    }

    /******************************************* count ***************************************************************/
    /**
     * 执行count请求，返回CountResponse
     *
     * @param index
     * @param type
     * @param sourceBuilder
     * @return CountResponse
     */
    public SearchResponse count(String index, String type, SearchSourceBuilder sourceBuilder) {
        SearchRequest request = getCountRequest(index, type);
        return count(request, sourceBuilder);
    }

    /**
     * 执行count请求，返回CountResponse
     *
     * @param indices
     * @param types
     * @param sourceBuilder
     * @return CountResponse
     */
    public SearchResponse count(String[] indices, String[] types, SearchSourceBuilder sourceBuilder) {
        SearchRequest request = getCountRequest(indices, types);

        return count(request, sourceBuilder);
    }

    /**
     * 执行count请求，返回CountResponse
     *
     * @param request
     * @param sourceBuilder
     * @return CountResponse
     */
    public SearchResponse count(SearchRequest request, SearchSourceBuilder sourceBuilder) {
        if (null != sourceBuilder) {
            request.source(sourceBuilder);
        }

        return count(request);
    }

    /**
     * 执行count请求，返回CountResponse
     *
     * @param request
     * @return CountResponse
     */
    public SearchResponse count(SearchRequest request) {
        return esClient.getClient().search(request).actionGet();
    }

    /******************************************* search ***************************************************************/
    /**
     * 执行search请求，返回SearchResponse
     *
     * @param index
     * @param type
     * @param sourceBuilder
     * @return SearchResponse
     */
    public SearchResponse search(String index, String type, SearchSourceBuilder sourceBuilder) {
        return search(new String[]{index}, new String[]{type}, sourceBuilder);
    }

    /**
     * 执行search请求，返回SearchResponse
     *
     * @param indices
     * @param types
     * @param sourceBuilder
     * @return SearchResponse
     */
    public SearchResponse search(String[] indices, String[] types, SearchSourceBuilder sourceBuilder) {
        SearchRequest request = getSearchRequest(indices, types);

        return search(request, sourceBuilder);
    }

    /**
     * 执行search请求，返回SearchResponse
     *
     * @param request
     * @param sourceBuilder
     * @return SearchResponse
     */
    public SearchResponse search(SearchRequest request, SearchSourceBuilder sourceBuilder) {
        request.source(sourceBuilder);

        return search(request);
    }

    /**
     * 执行search请求，返回SearchResponse
     *
     * @param request
     * @return SearchResponse
     */
    public SearchResponse search(SearchRequest request) {
        return esClient.getClient().search(request).actionGet();
    }

    public ElasticClient getEsClient() {
        return esClient;
    }

}
