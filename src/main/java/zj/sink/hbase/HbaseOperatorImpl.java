package zj.sink.hbase;

import com.thunisoft.sync.sink.hbase.domain.*;
import lombok.Data;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.filter.CompareFilter;
import org.apache.hadoop.hbase.filter.Filter;
import org.apache.hadoop.hbase.filter.FilterList;
import org.apache.hadoop.hbase.filter.SingleColumnValueFilter;
import org.apache.hadoop.hbase.io.compress.Compression;
import org.apache.hadoop.hbase.util.Bytes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.*;
import java.util.concurrent.*;

/**
 * Created by lk on 2017/1/9.
 */
@Component
@DependsOn("Constants")
@ConfigurationProperties
@PropertySource("classpath:ws-hbase.properties")
@Data
public class HbaseOperatorImpl {
    private static Logger logger = LoggerFactory.getLogger(HbaseOperatorImpl.class);

    private static Connection hconnection;

    @Autowired
    private Constants Constants;

    private static Admin admin;

    private static ExecutorService workerPool;

    private static ConcurrentMap<String, BufferedMutator> map = new ConcurrentHashMap<String, BufferedMutator>();

    private static Semaphore semaphore;

    private static int WAIT_TABLE_RESION_SIZE = 4;

    public static boolean sendRequest = false;

//    static {
//        try {
//            Configuration conf = HBaseConfiguration.create();
//            hconnection = ConnectionFactory.createConnection(conf);
//        } catch (IOException e) {
//            logger.error(new LogMessage().setInfo("初始化hbase出错， IOException ：" + e.getMessage()).toString());
//        }
//    }

    private Admin getHbaseAdmin() throws IOException {
        if (admin == null) {
            admin = hconnection.getAdmin();
        }
        return admin;
    }

//    public HbaseOperatorImpl() {
////        try {
////            creatTable(Constants.HBASE_TABLE_NAME, Constants.HBASE_FAMILY, Constants.HBASE_CONTENT,
////                Constants.REGION_SIZE);
////            if (Constants.IS_DATA_MANAGE) {
////                creatTable(Constants.HBASE_TABLE_NAME_FYW, Constants.HBASE_FAMILY, Constants.HBASE_CONTENT,
////                    WAIT_TABLE_RESION_SIZE);
////            }
////        } catch (IOException e) {
////            logger.error(new LogMessage().setInfo("创建hbase表出错， IOException ：" + e.getMessage()).toString());
////        }
//    }

    @PostConstruct
    public void init() {
        workerPool = Executors.newFixedThreadPool(Constants.poolSize);
        semaphore = new Semaphore(Constants.poolSize * 2);

        try {
            Configuration conf = HBaseConfiguration.create();
            hconnection = ConnectionFactory.createConnection(conf);
        } catch (IOException e) {
            logger.error(new LogMessage().setInfo("初始化hbase出错， IOException ：" + e.getMessage()).toString());
        }

        try {
            creatTable(Constants.hbaseTableName, Constants.hbaseFamily, Constants.hbaseContent,
                    Constants.regionSize);
            if (Constants.isDataManage) {
                creatTable(Constants.hbaseTableNameFyw, Constants.hbaseFamily, Constants.hbaseContent,
                        WAIT_TABLE_RESION_SIZE);
            }
        } catch (IOException e) {
            logger.error(new LogMessage().setInfo("创建hbase表出错， IOException ：" + e.getMessage()).toString());
        }
    }

    /**
     * 查看表是否存在
     * @param tableName 表名
     * @return boolean
     * @throws IOException
     */
    public boolean tableExists(String tableName) throws IOException {
        return tableExists(TableName.valueOf(tableName));
    }

    public boolean tableExists(TableName tableName) throws IOException {
        return getHbaseAdmin().tableExists(tableName);
    }

    /**
     * 创建表
     *
     * @tableName 表名
     *
     * @family 列族列表
     */
    public void creatTable(String tableName, String family, String contentFamily, int regionSize) throws IOException {
        if (tableExists(tableName)) {
            logger.info(new LogMessage().setAction("初始化表").setInfo(tableName + " 表已经存在").toString());
            return;
        }
        HTableDescriptor desc = new HTableDescriptor(TableName.valueOf(tableName));
        desc.addCoprocessor("org.apache.hadoop.hbase.coprocessor.AggregateImplementation");
        HColumnDescriptor columnDesc = new HColumnDescriptor(family);
        columnDesc.setCompressionType(Compression.Algorithm.SNAPPY);
        columnDesc.setMaxVersions(1);
        desc.addFamily(columnDesc);
        HColumnDescriptor columnContent = new HColumnDescriptor(contentFamily);
        columnContent.setCompressionType(Compression.Algorithm.SNAPPY);
        columnContent.setMaxVersions(1);
        desc.addFamily(columnContent);
        byte[][] splitKeys = initRegions(regionSize);
        admin.createTable(desc, splitKeys);
        logger.info(new LogMessage().setAction("初始化表").setInfo(tableName + " 表创建成功").toString());
    }

    /**
     * 预分区设置
     * @param mod
     * @return
     */
    private byte[][] initRegions(int mod) {
        byte[][] splitKeys = new byte[mod - 1][];

        DecimalFormat df = new DecimalFormat("000");
        for (int i = 0; i < mod - 1; i++) {
            splitKeys[i] = Bytes.add(Bytes.toBytes(df.format(i + 1)), Bytes.toBytes("0000000|"));
        }
        return splitKeys;
    }

    /**
     * 删除表
     * @param tableNameS
     * @throws IOException
     */
    public void deleteTable(String tableNameS) throws IOException {
        TableName tableName = TableName.valueOf(tableNameS);
        if (tableExists(tableName)) {
            admin.deleteTable(tableName);
            logger.info(new LogMessage().setAction("删除表").setInfo(tableName + " 表删除成功").toString());
        }
        logger.info(new LogMessage().setAction("删除表").setInfo(tableName + "不存在").toString());
    }

    /**
     * 插入数据
     * @param tableName
     * @param put
     * @throws IOException
     */
    public void addData(final String tableName, final Put put) throws IOException {

        try {
            semaphore.acquire();
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }
        workerPool.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Table htable = hconnection.getTable(TableName.valueOf(tableName));
                    htable.put(put);
                    htable.close();
                } catch (IOException e) {
                    logger.info(new LogMessage().setAction("插入数据失败").setInfo("IOException: " + e.getMessage())
                            .setAction_status(Constants.EXCEPRION).toString());
                } finally {
                    semaphore.release();

                }
            }
        });
    }

    /**
     * 批量插入
     * @param tableName
     * @param put
     * @throws IOException
     */
    public void addDataByBuffer(final String tableName, final Put put) throws IOException {
        try {
            semaphore.acquire();
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }
        workerPool.submit(new Callable<Void>() {
            @Override
            public Void call() {
                try {
                    getMutator(tableName).mutate(put);
                } catch (IOException e) {
                    logger.info(new LogMessage().setAction("插入数据失败").setInfo("IOException: " + e.getMessage())
                            .setAction_status(Constants.EXCEPRION).toString());
                } finally {
                    semaphore.release();
                }
                return null;
            }
        });
    }

    /**
     * 插入数据
     * @param tableName
     * @param document
     * @throws IOException
     */
    public void addDocData(final String tableName, final Document document) {
        try {
            semaphore.acquire();
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }
        workerPool.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Table htable = hconnection.getTable(TableName.valueOf(tableName));
                    htable.put(getPut(document));
                    htable.close();
                } catch (Exception e) {
                    logger.info("插入数据失败" + ExceptionUtils.getFullStackTrace(e));
                } finally {
                    semaphore.release();
//                    if (sendRequest && semaphore.availablePermits() == Constants.POOL_SIZE * 2) {
//                        logger.info("触发第二阶段");
//                        SentRequestUtils.sendRequest();
//                        sendRequest = false;
//                    }
                }
            }
        });
    }

    /**
     * 批量插入
     * @param tableName
     * @param document
     * @throws IOException
     */
    public void addDocDataByBuffer(final String tableName, final Document document) {
        try {
            semaphore.acquire();
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }
        workerPool.submit(new Callable<Void>() {
            @Override
            public Void call() {
                try {
                    getMutator(tableName).mutate(getPut(document));
                } catch (IOException e) {
                    logger.info(new LogMessage().setAction("插入数据失败").setInfo("IOException: " + e.getMessage())
                            .setRelatedBH("rowkey为：" + document.getOld_rowkey()).setAction_status(Constants.EXCEPRION)
                            .toString());
                } finally {
                    semaphore.release();
                }
                return null;
            }
        });
    }

    private BufferedMutator getMutator(String tableName) {
        if (map.containsKey(tableName)) {
            return map.get(tableName);
        } else {
            BufferedMutator mutator = null;
            BufferedMutatorParams params = new BufferedMutatorParams(TableName.valueOf(tableName))
                    .writeBufferSize(Constants.getBufferSize());
            try {
                mutator = hconnection.getBufferedMutator(params);
                map.put(tableName, mutator);
            } catch (IOException e) {
                logger.error(
                    new LogMessage().setInfo("初始化hbase BufferedMutator出错， IOException ：" + e.getMessage()).toString());
            }
            return mutator;
        }
    }

    private Put getPut(Document document) {
        Put put = new Put(Bytes.toBytes(document.getRowkey()));
        String title = document.getTitle();
        if (StringUtils.isNotBlank(title)) {
            if (title.contains(".txt")) {
                title = title.substring(0, title.lastIndexOf(".txt"));
            }
            put.addColumn(Bytes.toBytes(Constants.hbaseFamily), Bytes.toBytes(Constants.hbaseTitleCol),
                Bytes.toBytes(title));
        }

        put.addColumn(Bytes.toBytes(Constants.hbaseFamily), Bytes.toBytes(Constants.hbaseEnterTimeCol),
                Bytes.toBytes(document.getEnterUpdatetime()));
        collNotNull(document.getDocDate(), put, Constants.hbaseFamily, Constants.hbaseDateCol);
        collNotNull(document.getContent(), put, Constants.hbaseContent, Constants.hbaseContentCol);
        collNotNull(document.getLx(), put, Constants.hbaseFamily, Constants.hbaseLxCol);
        collNotNull(document.getNw(), put, Constants.hbaseFamily, Constants.hbaseNwCol);
        collNotNull(document.getUrlId(), put, Constants.hbaseFamily, Constants.hbaseUrlidCol);
        collNotNull(document.getOld_rowkey(), put, Constants.hbaseFamily, Constants.hbaseUrlidOldRoekey);
        collNotNull(document.getCurrentAH(), put, Constants.hbaseFamily, Constants.hbaseCurrentah);
        collNotNull(document.getAjlb(), put, Constants.hbaseFamily, Constants.hbaseAjlbCol);
        collNotNull(document.getXzqh(), put, Constants.hbaseFamily, Constants.hbaseXzquCol);
        collNotNull(document.getAddress(), put, Constants.hbaseFamily, Constants.hbaseAddressCol);
        collNotNull(document.getSource(), put, Constants.hbaseFamily, Constants.hbaseSourceCol);

//        if (CollectionUtils.isNotEmpty(document.getAhs()) && document.getAhs().size() > 0) {
//            String refah = StringUtils.join(document.getAhs(), Constants.   SEPARATOR_SEPARATOR);
//            put.addColumn(Bytes.toBytes(Constants.hbaseFamily), Bytes.toBytes(Constants.hbaseRefah),
//                Bytes.toBytes(refah));
//        } else {
//            put.addColumn(Bytes.toBytes(Constants.hbaseFamily), Bytes.toBytes(Constants.hbaseRefah),
//                Bytes.toBytes(""));
//        }
//        put.addColumn(Bytes.toBytes(Constants.hbaseFamily), Bytes.toBytes(Constants.hbaseUpdatetimeCol),
//            Bytes.toBytes(document.getUpdatetime()));

        return put;
    }

    private void collNotNull(String dataValue, Put put, String hbaseFamily, String hbaseDataCol) {
        if (StringUtils.isNotBlank(dataValue)) {
            put.addColumn(Bytes.toBytes(hbaseFamily), Bytes.toBytes(hbaseDataCol),
                    Bytes.toBytes(dataValue));
        }
    }


    public void scanTableByTimeStamp(String tableName, long minStamp, long maxStamp,
            List<Family> familys) throws IOException {
        scanTable(tableName, familys, minStamp, maxStamp, null, null);
    }

    public void scanTableByRow(String tableName, String startRow, String stopRow,
            List<Family> familys) throws IOException {
        scanTableByRow(tableName, Bytes.toBytes(startRow), Bytes.toBytes(stopRow), familys);
    }

    public void scanTableByRow(String tableName, byte[] startRow, byte[] stopRow,
            List<Family> familys) throws IOException {
        scanTable(tableName, familys, -1, -1, startRow, stopRow);
    }

    /**
     * 组织scan的返回列族或者返回列
     * @param familys
     * @param scan
     */
    private void getFamilys(List<Family> familys, Scan scan) {
        if (!familys.isEmpty()) {
            for (Family f : familys) {
                String columnFamily = f.getColumnFamily();
                List<String> columns = f.getCloumns();
                if (StringUtils.isNotBlank(columnFamily))
                    scan.addFamily(Bytes.toBytes(columnFamily));
                if (!columns.isEmpty()) {
                    for (String column : columns) {
                        scan.addColumn(Bytes.toBytes(columnFamily), Bytes.toBytes(column));
                    }
                }
            }
        }
    }

    public ResultScanner scanTable(String tableName, List<Family> familys, long minStamp, long maxStamp,
            byte[] startRow, byte[] stopRow) throws IOException {
        Table table = hconnection.getTable(TableName.valueOf(tableName));
        Scan scan = new Scan();
        if (startRow.length > 0)
            scan.setStartRow(startRow);
        if (stopRow.length > 0)
            scan.setStopRow(stopRow);
        if (minStamp >= 0 && maxStamp >= 0)
            scan.setTimeRange(minStamp, maxStamp);
        getFamilys(familys, scan);
        ResultScanner rs = table.getScanner(scan);
        table.close();
        return rs;
    }

    public ResultScanner scanTableByFilter(String tableName, FilterList.Operator relation,
            List<SingleColumnFilter> singleColumnFilters) throws IOException {
        return scanTableByFilter(tableName, relation, singleColumnFilters, null);
    }

    public ResultScanner scanTableByFilter(String tableName, FilterList.Operator relation,
            List<SingleColumnFilter> singleColumnFilters, String row) throws IOException {
        Table table = hconnection.getTable(TableName.valueOf(tableName));
        Scan scan = new Scan();
        if (StringUtils.isNotBlank(row)) {
            scan.setStartRow(Bytes.toBytes(row));
        }
        FilterList list = new FilterList(relation);

        for (SingleColumnFilter scf : singleColumnFilters) {
            CompareFilter.CompareOp compareOpeara = getCompareOp(scf.getCompareOp());
            Filter filter = new SingleColumnValueFilter(Bytes.toBytes(scf.getFamily()),
                    Bytes.toBytes(scf.getQualifier()), compareOpeara, Bytes.toBytes(scf.getValue()));
            list.addFilter(filter);
        }
        scan.setFilter(list);
        return table.getScanner(scan);
    }

    private CompareFilter.CompareOp getCompareOp(SingleColumnFilter.CompareOpeara opeara) {
        if (CompareFilter.CompareOp.LESS_OR_EQUAL.toString().equals(opeara.toString()))
            return CompareFilter.CompareOp.LESS_OR_EQUAL;
        else if (CompareFilter.CompareOp.GREATER.toString().equals(opeara.toString()))
            return CompareFilter.CompareOp.GREATER;
        else if (CompareFilter.CompareOp.LESS.toString().equals(opeara.toString()))
            return CompareFilter.CompareOp.LESS;
        else if (CompareFilter.CompareOp.EQUAL.toString().equals(opeara.toString()))
            return CompareFilter.CompareOp.EQUAL;
        else if (CompareFilter.CompareOp.NOT_EQUAL.toString().equals(opeara.toString()))
            return CompareFilter.CompareOp.NOT_EQUAL;
        else if (CompareFilter.CompareOp.GREATER_OR_EQUAL.toString().equals(opeara.toString()))
            return CompareFilter.CompareOp.GREATER_OR_EQUAL;
        else if (CompareFilter.CompareOp.NO_OP.toString().equals(opeara.toString()))
            return CompareFilter.CompareOp.NO_OP;
        return null;
    }

    public Result getData(String tableName, String rowkey) throws IOException {
        return getData(tableName, rowkey, null);
    }

    public Result getData(String tableName, String rowkey, String family) throws IOException {
        return getData(tableName, rowkey, family, null);
    }

    public Result getData(String tableName, String rowkey, String family, List<String> qualifiers) throws IOException {
        Table table = hconnection.getTable(TableName.valueOf(tableName));
        Get get = dealGet(rowkey, family, qualifiers);
        Result result = table.get(get);
        table.close();
        return result;
    }

    /**
     * 批量获取数据
     * @param tableName 表名
     * @param rowkeys 行键列表
     * @param family 列族
     * @param qualifiers 列
     * @return
     * @throws IOException
     */
    public Result[] getData(String tableName, List<String> rowkeys, String family,
            List<String> qualifiers) throws IOException {
        Table table = hconnection.getTable(TableName.valueOf(tableName));

        List<Get> gets = new ArrayList<Get>();

        for (int i = 0; rowkeys.size() > 0 && i < rowkeys.size(); i++) {
            Get get = dealGet(rowkeys.get(i), family, qualifiers);
            gets.add(get);
        }
        Result[] result = table.get(gets);
        table.close();
        return result;
    }

    private Get dealGet(String rowkey, String family, List<String> qualifiers) {
        Get get = new Get(Bytes.toBytes(rowkey));
        if (StringUtils.isNotBlank(family)) {
            get.addFamily(Bytes.toBytes(family));
            if (qualifiers != null && qualifiers.size() > 0) {
                for (String qualifier : qualifiers) {
                    get.addColumn(Bytes.toBytes(family), Bytes.toBytes(qualifier));
                }
            }
        }
        return get;
    }

    public static void main(String[] args) throws IOException {
        HbaseOperatorImpl hbaseOperatorImpl = new HbaseOperatorImpl();
        hbaseOperatorImpl.deleteTable("111111111111");
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.thunisoft.system.service.HbaseOperatorI#getDocument(java.lang.String,
     * java.lang.String)
     */
    public Document getDocument(String tableName, String rowkey) throws IOException {
        Document writ = null;
        try {
            Table table = hconnection.getTable(TableName.valueOf(tableName));
            Get get = new Get(Bytes.toBytes(rowkey));
            Result row = table.get(get);
            table.close();
            String title = Bytes.toString(
                row.getValue(Bytes.toBytes(Constants.hbaseFamily), Bytes.toBytes(Constants.hbaseTitleCol)));
            String content = Bytes.toString(
                row.getValue(Bytes.toBytes(Constants.hbaseContent), Bytes.toBytes(Constants.hbaseContentCol)));
            if (row == null || StringUtils.isBlank(content)) {
                logger.info("【丢失】从文书最终xml表中获取文书内容为空！ rowkey:{}", rowkey);
            }
            long updateTime = System.currentTimeMillis();
            String date = Bytes.toString(
                row.getValue(Bytes.toBytes(Constants.hbaseFamily), Bytes.toBytes(Constants.hbaseDateCol)));
            String lx = Bytes.toString(
                row.getValue(Bytes.toBytes(Constants.hbaseFamily), Bytes.toBytes(Constants.hbaseLxCol)));
            String nw = Bytes.toString(
                row.getValue(Bytes.toBytes(Constants.hbaseFamily), Bytes.toBytes(Constants.hbaseNwCol)));
            String urlID = Bytes.toString(
                row.getValue(Bytes.toBytes(Constants.hbaseFamily), Bytes.toBytes(Constants.hbaseUrlidCol)));
            String oldRowKey = Bytes.toString(
                row.getValue(Bytes.toBytes(Constants.hbaseFamily), Bytes.toBytes(Constants.hbaseUrlidOldRoekey)));
            String ajlb = Bytes.toString(
                row.getValue(Bytes.toBytes(Constants.hbaseFamily), Bytes.toBytes(Constants.hbaseAjlbCol)));
            String xzqh = Bytes.toString(
                row.getValue(Bytes.toBytes(Constants.hbaseFamily), Bytes.toBytes(Constants.hbaseXzquCol)));
            String currentAH = Bytes.toString(
                row.getValue(Bytes.toBytes(Constants.hbaseFamily), Bytes.toBytes(Constants.hbaseCurrentah)));
            String refAhs = Bytes.toString(
                row.getValue(Bytes.toBytes(Constants.hbaseFamily), Bytes.toBytes(Constants.hbaseRefah)));
            Set<String> refAhSet = new HashSet<String>();
            if (StringUtils.isNotEmpty(refAhs)) {
                    String[] refAhArray = refAhs.split(Constants.CHAR_SPLIT);
                for (String refAh : refAhArray) {
                    refAhSet.add(refAh);
                }
            }
            writ = new Document(rowkey, title, content, updateTime, date, lx, nw, urlID, oldRowKey, ajlb, xzqh,
                    currentAH, refAhSet);
        } catch (Exception e) {
            logger.error("获取文书内容异常,文书id{}!", rowkey, e);
        }
        return writ;
    }

}
