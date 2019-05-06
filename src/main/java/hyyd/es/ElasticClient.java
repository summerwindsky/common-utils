package hyyd.es;

import static java.util.stream.Collectors.toList;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Stream;

import org.apache.commons.lang.StringUtils;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.xcontent.NamedXContentRegistry;
import org.elasticsearch.index.reindex.ReindexPlugin;
import org.elasticsearch.join.ParentJoinPlugin;
import org.elasticsearch.percolator.PercolatorPlugin;
import org.elasticsearch.plugins.Plugin;
import org.elasticsearch.plugins.PluginsService;
import org.elasticsearch.plugins.SearchPlugin;
import org.elasticsearch.script.mustache.MustachePlugin;
import org.elasticsearch.search.SearchModule;
import org.elasticsearch.transport.Netty3Plugin;
import org.elasticsearch.transport.Netty4Plugin;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.elasticsearch.xpack.client.PreBuiltXPackTransportClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author gaojun
 * @Title ElasticClient
 * @Description TODO
 * @date 2015-8-12 下午6:16:02
 * @Company 北京华宇信息技术有限公司
 */

public class ElasticClient {

    private static final Logger logger = LoggerFactory.getLogger(ElasticClient.class);

    private static Map<String, ElasticClient> m_instance = new HashMap<String, ElasticClient>();

    private Client esClient = null;

    private String clusterName = "";

    private String ip = "";

    private int port = 9300;

    private Settings settings;

    private NamedXContentRegistry xContentRegistry;

    private String xpackSecurityUser = null;

    private static final Collection<Class<? extends Plugin>> PRE_INSTALLED_PLUGINS = Collections
            .unmodifiableList(Arrays.asList(Netty3Plugin.class, Netty4Plugin.class, ReindexPlugin.class,
                PercolatorPlugin.class, MustachePlugin.class, ParentJoinPlugin.class));

    private ElasticClient(String clusterName, String xpackSecurityUser, String ip, int port) {
        this.clusterName = clusterName;
        this.xpackSecurityUser = xpackSecurityUser;
        this.ip = ip;
        this.port = port;
    }

    public static synchronized ElasticClient getInstance(String clusterName, String xpackSecurityUser, String ip,
            int port) {
        ElasticClient instance = m_instance.get(clusterName);
        if (null == instance) {
            instance = new ElasticClient(clusterName, xpackSecurityUser, ip, port);
            m_instance.put(clusterName, instance);
        }
        instance.connect();
        return instance;
    }

    public static synchronized ElasticClient getInstance(String clusterName, String ip, int port) {
        return getInstance(clusterName, null, ip, 9300);
    }

    public static ElasticClient getInstance(String clusterName, String xpackSecurityUser, String ip) {
        return getInstance(clusterName, xpackSecurityUser, ip, 9300);
    }

    public static ElasticClient getInstance(String clusterName, String ip) {
        return getInstance(clusterName, null, ip);
    }

    public static void shutdown() {
        for (ElasticClient instance : m_instance.values()) {
            if (instance != null) {
                instance.close();
                instance = null;
            }
        }
    }

    public synchronized void close() {
        if (null != esClient) {
            esClient.close();
            esClient = null;
        }
    }

    @SuppressWarnings("resource")
    private synchronized void connect() {
        if (esClient == null) {
            logger.info("ElasticClient clusterName[{}] connecting...", clusterName);
            try {
                // 设置client.transport.sniff为true来使客户端去嗅探整个集群的状态，把集群中其它机器的ip地址加到客户端中，
                // 这样做的好处是一般你不用手动设置集群里所有集群的ip到连接客户端，它会自动帮你添加，并且自动发现新加入集群的机器。
                Settings.Builder builder = Settings.builder().put("cluster.name", clusterName)
                        .put("client.transport.sniff", true);
                if (StringUtils.isNotBlank(xpackSecurityUser)) {
                    builder.put("xpack.security.transport.ssl.enabled", false).put("xpack.security.user",
                        xpackSecurityUser);
                }
                settings = builder.build();
                buildTemplate(settings);

                if (StringUtils.isBlank(xpackSecurityUser)) {
                    esClient = new PreBuiltTransportClient(settings)
                            .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(ip), port));
                } else {
                    esClient = new PreBuiltXPackTransportClient(settings)
                            .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(ip), port));
                }

                logger.info("ElasticClient clusterName[{}] connected", clusterName);
            } catch (Exception e) {
                close();
                logger.error("ElasticClient clusterName[{}] connect error", clusterName);
                logger.error("", e);
            }
        }
    }

    private void buildTemplate(Settings settings) {
        final PluginsService pluginsService = new PluginsService(settings, null, null, PRE_INSTALLED_PLUGINS);

        SearchModule searchModule = new SearchModule(settings, false, pluginsService.filterPlugins(SearchPlugin.class));
        xContentRegistry = new NamedXContentRegistry(Stream
                .of(searchModule.getNamedXContents().stream(),
                    pluginsService.filterPlugins(Plugin.class).stream().flatMap(p -> p.getNamedXContent().stream()))
                .flatMap(Function.identity()).collect(toList()));
    }

    public String getIp() {
        return this.ip;
    }

    public int getPort() {
        return this.port;
    }

    public Settings getSettings() {
        return this.settings;
    }

    public NamedXContentRegistry getxContentRegistry() {
        return this.xContentRegistry;
    }

    public void reConnect() {
        close();
        connect();
    }

    public String getClusterName() {
        return this.clusterName;
    }

    public Client getClient() {
        return esClient;
    }

    public static void main(String[] args) {
        try {
            InetAddress addr = InetAddress.getByName("172.16.6.212");
            logger.error(addr.getHostAddress());
        } catch (UnknownHostException e) {
            logger.error("", e);
        }
    }

}
