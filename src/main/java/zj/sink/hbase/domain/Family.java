package zj.sink.hbase.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * Title: Family
 * Description: 
 * Company: 北京华宇元典信息服务有限公司
 *
 * @author tianxiupeng
 * @version 1.0
 * @date 2018年7月31日 下午3:02:18
 *
 */
public class Family {
    private String columnFamily;

    private List<String> cloumns = new ArrayList<String>();

    public String getColumnFamily() {
        return columnFamily;
    }

    public List<String> getCloumns() {
        return cloumns;
    }

    public void setColumnFamily(String columnFamily) {
        this.columnFamily = columnFamily;
    }

    public void setCloumns(List<String> cloumns) {
        this.cloumns = cloumns;
    }
}
