package zj.sink.hbase;

import com.thunisoft.sync.sink.hbase.domain.Document;
import com.thunisoft.sync.sink.hbase.domain.SingleColumnFilter;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.filter.FilterList;

import java.io.IOException;
import java.util.List;

/**
 * Created by lk on 2017/1/9.
 */
public interface HbaseOperatorI {

    /**
     * 根据过滤条件遍历hbase数据表
     * @param tableName
     * @param relation
     * @param singleColumnFilters
     * @return
     * @throws IOException
     */
    ResultScanner scanTableByFilter(String tableName, FilterList.Operator relation,
                                    List<SingleColumnFilter> singleColumnFilters) throws IOException;

    /**
     * 根据过滤条件和开始rowKey遍历hbase数据表
     * @param tableName
     * @param relation
     * @param singleColumnFilters
     * @param row
     * @return
     * @throws IOException
     */
    ResultScanner scanTableByFilter(String tableName, FilterList.Operator relation,
                                    List<SingleColumnFilter> singleColumnFilters, String row) throws IOException;

    void addData(final String tableName, final Put put) throws IOException;

    void addDataByBuffer(final String tableName, final Put put) throws IOException;

    void addDocDataByBuffer(final String tableName, final Document document);

    void addDocData(final String tableName, final Document document);

    Result getData(String tableName, String rowkey, String family, List<String> list) throws IOException;

    Result getData(String tableName, String rowkey) throws IOException;

    Document getDocument(String tableName, String rowkey) throws IOException;

    Result getData(String tableName, String rowkey, String family) throws IOException;
}
