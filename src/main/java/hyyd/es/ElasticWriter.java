package hyyd.es;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.elasticsearch.action.admin.indices.settings.put.UpdateSettingsRequest;
import org.elasticsearch.action.admin.indices.settings.put.UpdateSettingsResponse;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteRequestBuilder;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.support.WriteRequest.RefreshPolicy;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateRequestBuilder;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.Requests;
import org.elasticsearch.common.Strings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.settings.Settings.Builder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.reindex.BulkByScrollResponse;
import org.elasticsearch.index.reindex.DeleteByQueryAction;
import org.elasticsearch.index.reindex.DeleteByQueryRequest;
import org.elasticsearch.index.reindex.DeleteByQueryRequestBuilder;
import org.elasticsearch.script.Script;

import com.thunisoft.elasticsearch.client.ElasticClient;

/**
 * @Title ElesticWriter
 * @Description es操作类-增删改
 * @author gaojun
 * @date 2015-8-12 下午5:37:15
 * @Company 北京华宇信息技术有限公司
 */

public class ElasticWriter {

    private ElasticClient esClient = null;

    public ElasticWriter(String clusterName, String xpackSecurityUser, String ip, int port) {
        esClient = ElasticClient.getInstance(clusterName, xpackSecurityUser, ip, port);
    }

    public ElasticWriter(String clusterName, String ip, int port) {
        esClient = ElasticClient.getInstance(clusterName, null, ip, port);
    }

    public ElasticWriter(String clusterName, String ip) {
        esClient = ElasticClient.getInstance(clusterName, null, ip);
    }

    public ElasticWriter(String clusterName, String xpackSecurityUser, String ip) {
        esClient = ElasticClient.getInstance(clusterName, xpackSecurityUser, ip);
    }

    public ElasticWriter(ElasticClient esClient) {
        this.esClient = esClient;
    }

    /**
     * 更新索引库setting
     * 
     * @param indices 要更新的索引库，多个用英文逗号(,)分隔
     * @param settings 要更新的setting项，可以是Map、Settings(ImmutableSettings)、Settings.
     *            $Builder、String(json/yaml format)
     * @return
     */
    @SuppressWarnings("rawtypes")
    public UpdateSettingsResponse updateSettings(String indices, Object settings) {
        UpdateSettingsRequest request = Requests.updateSettingsRequest(Strings.splitStringByCommaToArray(indices));
        if (settings instanceof Map) {
            request.settings((Map) settings);
        } else if (settings instanceof Settings) {
            request.settings((Settings) settings);
        } else if (settings instanceof Builder) {
            request.settings((Builder) settings);
        } else if (settings instanceof String) {
            request.settings((String) settings, XContentFactory.xContentType((String) settings));
        } else {
            return null;
        }
        return esClient.getClient().admin().indices().updateSettings(request).actionGet();
    }

    /********************************************** insert **************************************************************/
    /**
     * 获取IndexRequest
     * 
     * @param index 索引库
     * @param type 索引类型
     * @param id 可以为null，null即为自动生成id
     * @param source 索引内容，支持Map或者String(json)
     * @return
     */
    public IndexRequest getIndexRequest(String index, String type, String id, Object source) {
        return getIndexRequest(index, type, null, id, source);
    }

    /**
     * 获取IndexRequest
     * 
     * @param index 索引库
     * @param type 索引类型
     * @param pid 父文档id,可以为null，null即为没有父文档
     * @param id 可以为null，null即为自动生成id
     * @param source 索引内容，支持Map或者String(json)
     * @return
     */
    public IndexRequest getIndexRequest(String index, String type, String pid, String id, Object source) {
        IndexRequestBuilder builder = getIndexRequestBuilder(index, type, pid, id, source);
        if (null == builder) {
            return null;
        }
        return builder.request();
    }

    /**
     * 获取IndexRequest
     * 
     * @param index 索引库
     * @param type 索引类型
     * @param pid 父文档id,可以为null，null即为没有父文档
     * @param id 可以为null，null即为自动生成id
     * @param source 索引内容，支持Map或者String(json)
     * @param refresh 强制刷新
     * @return
     */
    public IndexRequest getIndexRequest(String index, String type, String pid, String id, Object source,
            boolean refresh) {
        IndexRequestBuilder builder = getIndexRequestBuilder(index, type, pid, id, source, refresh);
        if (null == builder) {
            return null;
        }
        return builder.request();
    }

    /**
     * 获取IndexRequest，id自动生成
     * 
     * @param index 索引库
     * @param type 索引类型
     * @param source 索引内容，支持Map或者String(json)
     * @return
     */
    public IndexRequest getIndexRequest(String index, String type, Object source) {
        return getIndexRequest(index, type, null, source);
    }

    /**
     * @param index 索引库
     * @param type 索引类型
     * @param pid 父文档id,可以为null，null即为没有父文档
     * @param id 可以为null，null即为自动生成id
     * @param source 索引内容，支持Map或者String(json)
     * @param refresh 强制刷新
     * @return
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public IndexRequestBuilder getIndexRequestBuilder(String index, String type, String pid, String id, Object source,
            boolean refresh) {
        if (null == source) {
            return null;
        }
        IndexRequestBuilder builder = esClient.getClient().prepareIndex(index, type, id);
        if (source instanceof Map) {
            builder.setSource((Map) source);
        } else if (source instanceof String) {
            builder.setSource((String) source, XContentFactory.xContentType((String) source));
        }
        if (StringUtils.isNotBlank(pid)) {
            builder.setParent(pid);
        }
        if (refresh) {
            builder.setRefreshPolicy(RefreshPolicy.IMMEDIATE);
        }
        return builder;
    }

    /**
     * @param index 索引库
     * @param type 索引类型
     * @param pid 父文档id,可以为null，null即为没有父文档
     * @param id 可以为null，null即为自动生成id
     * @param source 索引内容，支持Map或者String(json)
     * @return
     */
    public IndexRequestBuilder getIndexRequestBuilder(String index, String type, String pid, String id, Object source) {
        return getIndexRequestBuilder(index, type, pid, id, source, false);
    }

    /**
     * 索引单条数据
     * 
     * @param index
     * @param type
     * @param id 可以为null，null即为自动生成id
     * @param source 索引内容，支持Map或者String(json)
     * @return
     */
    public IndexResponse index(String index, String type, String id, Object source) {
        return index(index, type, null, id, source);
    }

    /**
     * 索引单条数据
     * 
     * @param index
     * @param type
     * @param pid 父文档id,可以为null，null即为没有父文档
     * @param id 可以为null，null即为自动生成id
     * @param source 索引内容，支持Map或者String(json)
     * @return
     */
    public IndexResponse index(String index, String type, String pid, String id, Object source) {
        return index(index, type, pid, id, source, false);
    }

    /**
     * 索引单条数据
     * 
     * @param index
     * @param type
     * @param pid 父文档id,可以为null，null即为没有父文档
     * @param id 可以为null，null即为自动生成id
     * @param source 索引内容，支持Map或者String(json)
     * @param refresh 强制刷新
     * @return
     */
    public IndexResponse index(String index, String type, String pid, String id, Object source, boolean refresh) {
        IndexRequestBuilder builder = getIndexRequestBuilder(index, type, pid, id, source, refresh);
        if (null == builder) {
            return null;
        }
        return builder.get();
    }

    /**
     * 索引单条数据,自动生成id
     * 
     * @param index
     * @param type
     * @param source 索引内容，支持Map或者String(json)
     * @return
     */
    public IndexResponse index(String index, String type, Object source) {
        return index(index, type, null, source);
    }

    /**
     * 批量项同一索引同一type中创建索引,refresh为false
     * 
     * @param index
     * @param type
     * @param pid
     * @param lst
     * @return
     */
    public BulkResponse indexByBulk(String index, String type, String pid, List<Object> lst) {
        if (CollectionUtils.isEmpty(lst)) {
            return null;
        }
        BulkRequestBuilder builder = getBulkRequestBuilder();
        addIndex(builder, index, type, pid, lst);

        return builder.get();
    }

    /***************************************** update ****************************************************/

    public UpdateRequestBuilder getUpdateRequestBuilder(String index, String type, String id, String idOrCode,
            Map<String, Object> sParam) {
        if (StringUtils.isBlank(idOrCode)) {
            return null;
        }
        UpdateRequestBuilder builder = esClient.getClient().prepareUpdate(index, type, id);
        Script script = new Script(Script.DEFAULT_SCRIPT_TYPE, Script.DEFAULT_SCRIPT_LANG, idOrCode, sParam);
        builder.setScript(script);
        return builder;
    }

    /**
     * 获取更新request
     * 
     * @param index
     * @param type
     * @param id
     * @param script 更新脚本
     * @param sParam 脚本参数
     * @return
     */
    public UpdateRequest getUpdateRequest(String index, String type, String id, String script,
            Map<String, Object> sParam) {
        UpdateRequestBuilder builder = getUpdateRequestBuilder(index, type, id, script, sParam);
        if (null == builder) {
            return null;
        }
        return builder.request();
    }

    /**
     * 更新文档
     * 
     * @param index
     * @param type
     * @param id
     * @param script 更新脚本
     * @param sParam 脚本参数
     * @return
     */
    public UpdateResponse update(String index, String type, String id, String script, Map<String, Object> sParam) {
        UpdateRequestBuilder builder = getUpdateRequestBuilder(index, type, id, script, sParam);
        if (null == builder) {
            return null;
        }
        return builder.get();
    }

    /**
     * 更新文档
     * 
     * @param index
     * @param type
     * @param id
     * @param source 更新内容，支持Map或者String(json)
     * @return
     */
    @SuppressWarnings("rawtypes")
    public UpdateRequestBuilder getUpdateRequestBuilder(String index, String type, String id, Object source) {
        if (null == source) {
            return null;
        }
        UpdateRequestBuilder builder = esClient.getClient().prepareUpdate(index, type, id);
        if (source instanceof Map) {
            builder.setDoc((Map) source);
        } else if (source instanceof String) {
            builder.setDoc((String) source, XContentFactory.xContentType((String) source));
        }
        return builder;
    }

    /**
     * 更新文档
     * 
     * @param index
     * @param type
     * @param id
     * @param source 更新内容，支持Map或者String(json)
     * @return
     */
    public UpdateRequest getUpdateRequest(String index, String type, String id, Object source) {
        return getUpdateRequestBuilder(index, type, id, source).request();
    }

    /**
     * 更新文档
     * 
     * @param index
     * @param type
     * @param id
     * @param source 更新内容，支持Map或者String(json)
     * @return
     */
    public UpdateResponse update(String index, String type, String id, Object source) {
        UpdateRequestBuilder builder = getUpdateRequestBuilder(index, type, id, source);
        if (null == builder) {
            return null;
        }
        return builder.get();
    }

    /************************************************ delete *********************************************************/

    /**
     * @param index
     * @param type
     * @param id
     * @return
     */
    public DeleteRequestBuilder getDeleteRequestBuilder(String index, String type, String id) {
        return getDeleteRequestBuilder(index, type, id, null);
    }

    /**
     * @param index
     * @param type
     * @param id
     * @param routing
     * @return
     */
    public DeleteRequestBuilder getDeleteRequestBuilder(String index, String type, String id, String routing) {
        return getDeleteRequestBuilder(index, type, id, routing, false);
    }

    /**
     * @param index
     * @param type
     * @param id
     * @param refresh 强制刷新
     * @return
     */
    public DeleteRequestBuilder getDeleteRequestBuilder(String index, String type, String id, boolean refresh) {
        return getDeleteRequestBuilder(index, type, id, null, refresh);
    }

    /**
     * @param index
     * @param type
     * @param id
     * @param routing
     * @param refresh 强制刷新
     * @return
     */
    public DeleteRequestBuilder getDeleteRequestBuilder(String index, String type, String id, String routing, boolean refresh) {
        DeleteRequestBuilder builder = esClient.getClient().prepareDelete(index, type, id);
        if (StringUtils.isNotBlank(routing)) {
            builder.setRouting(routing);
        }
        if (refresh) {
            builder.setRefreshPolicy(RefreshPolicy.IMMEDIATE);
        }
        return builder;
    }

    /**
     * @param index
     * @param type
     * @param id
     * @return
     */
    public DeleteRequest getDeleteRequest(String index, String type, String id) {
        DeleteRequestBuilder builder = getDeleteRequestBuilder(index, type, id);
        return builder.request();
    }

    /**
     * @param index
     * @param type
     * @param id
     * @param routing
     * @return
     */
    public DeleteRequest getDeleteRequest(String index, String type, String id, String routing) {
        DeleteRequestBuilder builder = getDeleteRequestBuilder(index, type, id, routing);
        return builder.request();
    }

    /**
     * @param index
     * @param type
     * @param id
     * @param refresh 强制刷新
     * @return
     */
    public DeleteRequest getDeleteRequest(String index, String type, String id, boolean refresh) {
        DeleteRequestBuilder builder = getDeleteRequestBuilder(index, type, id, refresh);
        return builder.request();
    }

    /**
     * @param index
     * @param type
     * @param id
     * @param routing
     * @param refresh 强制刷新
     * @return
     */
    public DeleteRequest getDeleteRequest(String index, String type, String id, String routing, boolean refresh) {
        DeleteRequestBuilder builder = getDeleteRequestBuilder(index, type, id, routing, refresh);
        return builder.request();
    }

    /**
     * @param index
     * @param type
     * @param id
     * @return
     */
    public DeleteResponse delete(String index, String type, String id) {
        DeleteRequestBuilder builder = getDeleteRequestBuilder(index, type, id);
        return builder.get();
    }

    /**
     * @param index
     * @param type
     * @param id
     * @param routing
     * @return
     */
    public DeleteResponse delete(String index, String type, String id, String routing) {
        DeleteRequestBuilder builder = getDeleteRequestBuilder(index, type, id, routing);
        return builder.get();
    }

    /**
     * @param index
     * @param type
     * @param id
     * @param refresh 强制刷新
     * @return
     */
    public DeleteResponse delete(String index, String type, String id, boolean refresh) {
        DeleteRequestBuilder builder = getDeleteRequestBuilder(index, type, id, refresh);
        return builder.get();
    }

    /**
     * @param index
     * @param type
     * @param id
     * @param routing
     * @param refresh 强制刷新
     * @return
     */
    public DeleteResponse delete(String index, String type, String id, String routing, boolean refresh) {
        DeleteRequestBuilder builder = getDeleteRequestBuilder(index, type, id, routing, refresh);
        return builder.get();
    }

    /**
     * 根据id批量删除同一索引同一type
     * 
     * @param index
     * @param type
     * @param lst
     * @return
     */
    public BulkResponse deleteByBulk(String index, String type, Collection<String> coll) {
        if (CollectionUtils.isEmpty(coll)) {
            return null;
        }
        BulkRequestBuilder builder = getBulkRequestBuilder();
        addDelete(builder, index, type, coll);

        return builder.get();
    }

    /**
     * @param indices 要更新的索引库，多个用英文逗号(,)分隔
     * @param query
     * @return
     */
    public DeleteByQueryRequestBuilder getDeleteByQueryRequestBuilder(String indices, Object query) {
        if (query == null) {
            return null;
        }
        QueryBuilder qBuilder = null;
        if (query instanceof QueryBuilder) {
            qBuilder = (QueryBuilder) query;
        } else if (query instanceof String) {
            qBuilder = QueryBuilders.queryStringQuery((String) query);
        }
        DeleteByQueryRequestBuilder builder = DeleteByQueryAction.INSTANCE.newRequestBuilder(esClient.getClient())
                .filter(qBuilder).source(Strings.splitStringByCommaToArray(indices));
        return builder;
    }

    /**
     * @param indices 要更新的索引库，多个用英文逗号(,)分隔
     * @param query
     * @return
     */
    public DeleteByQueryRequest getDeleteByQueryRequest(String indices, Object query) {
        DeleteByQueryRequestBuilder builder = getDeleteByQueryRequestBuilder(indices, query);
        if (null == builder) {
            return null;
        }
        return builder.request();
    }

    /**
     * @param indices 要更新的索引库，多个用英文逗号(,)分隔
     * @param query
     * @return
     */
    public BulkByScrollResponse deleteByQuery(String indices, Object query) {
        DeleteByQueryRequestBuilder builder = getDeleteByQueryRequestBuilder(indices, query);
        if (null == builder) {
            return null;
        }
        return builder.get();
    }

    /********************************************* bulk *******************************************************/

    /**
     * 获取BulkRequestBuilder
     * 
     * @return
     */
    public BulkRequestBuilder getBulkRequestBuilder() {
        BulkRequestBuilder builder = esClient.getClient().prepareBulk();
        return builder;
    }

    /**
     * 构造BulkRequestBuilder，添加request
     * 
     * @param builder BulkRequestBuilder
     * @param rObj request，支持IndexRequest、IndexRequestBuilder、UpdateRequest、
     *            UpdateRequestBuilder
     *            、DeleteRequest、DeleteRequestBuilder，以及由这几个组成的List
     * @return
     */
    @SuppressWarnings("rawtypes")
    public BulkRequestBuilder addRequestBuilder(BulkRequestBuilder builder, Object rObj) {
        if (null == rObj) {
            return builder;
        }
        if (rObj instanceof List) {
            for (Object obj : (List) rObj) {
                addRequest(builder, obj);
            }
        } else {
            addRequest(builder, rObj);
        }
        return builder;
    }

    /**
     * 构造BulkRequestBuilder，添加request
     * 
     * @param builder
     * @param rObj request ，支持IndexRequest、IndexRequestBuilder、UpdateRequest、
     *            UpdateRequestBuilder 、DeleteRequest、DeleteRequestBuilder
     * @return
     */
    public BulkRequestBuilder addRequest(BulkRequestBuilder builder, Object rObj) {
        if (null == rObj) {
            return builder;
        }
        if (rObj instanceof IndexRequest) {
            builder.add((IndexRequest) rObj);
        } else if (rObj instanceof IndexRequestBuilder) {
            builder.add((IndexRequestBuilder) rObj);
        } else if (rObj instanceof UpdateRequest) {
            builder.add((UpdateRequest) rObj);
        } else if (rObj instanceof UpdateRequestBuilder) {
            builder.add((UpdateRequestBuilder) rObj);
        } else if (rObj instanceof DeleteRequest) {
            builder.add((DeleteRequest) rObj);
        } else if (rObj instanceof DeleteRequestBuilder) {
            builder.add((DeleteRequestBuilder) rObj);
        }
        return builder;
    }

    public BulkRequestBuilder addIndex(BulkRequestBuilder builder, String index, String type, String pid, String id,
            Object source) {
        if (null == source) {
            return builder;
        }
        IndexRequestBuilder indexBuilder = getIndexRequestBuilder(index, type, pid, id, source);
        if (indexBuilder != null) {
            builder.add(indexBuilder);
        }
        return builder;
    }

    public BulkRequestBuilder addIndex(BulkRequestBuilder builder, String index, String type, String pid,
            List<Object> lst) {
        if (null == lst) {
            return builder;
        }
        for (Object obj : lst) {
            addIndex(builder, index, type, pid, null, obj);
        }
        return builder;
    }

    public BulkRequestBuilder addUpdate(BulkRequestBuilder builder, String index, String type, String id, String script,
            Map<String, Object> sParam) {
        UpdateRequestBuilder updateBuilder = getUpdateRequestBuilder(index, type, id, script, sParam);
        if (updateBuilder != null) {
            builder.add(updateBuilder);
        }
        return builder;
    }

    public BulkRequestBuilder addDelete(BulkRequestBuilder builder, String index, String type, String id) {
        DeleteRequestBuilder deleteBuilder = getDeleteRequestBuilder(index, type, id);
        if (null != deleteBuilder) {
            builder.add(deleteBuilder);
        }
        return builder;
    }

    public BulkRequestBuilder addDelete(BulkRequestBuilder builder, String index, String type,
            Collection<String> coll) {
        if (null == coll) {
            return builder;
        }
        for (String id : coll) {
            addDelete(builder, index, type, id);
        }
        return builder;
    }

    public ElasticClient getEsClient() {
        return esClient;
    }

}
