package zj.sink.hbase.domain;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/**
 * Created by lk on 2016/12/15.
 */
@Configuration("Constants")
@ConfigurationProperties
@PropertySource("classpath:ws-hbase.properties")
@Data
public class Constants {

    public String CHAR_SPLIT = "@@@@";
    public final String SEPARATOR_SEPARATOR = "@@@@";
    public String EXCEPRION = "异常";

    public int bufferSize;
    public int poolSize;

    public boolean isDataManage;

    public String isAddData;

    public String isAddSfxz;

    public String isAddRelationError;

    public String isAddRelation;

    public String hbaseTableName;
    public String hbaseTableNameFyw;

    public int regionSize;

    public String hbaseFamily;

    public String hbaseUpdatetimeFamily;

    public String hbaseContent;

    public String hbaseTitleCol;

    public String hbaseContentCol;

    public String hbaseUpdatetimeCol;

    public String hbaseDateCol;

    public String hbaseLxCol;

    public String hbaseNwCol;

    public String hbaseUrlidCol;

    public String hbaseUrlidOldRoekey;

    public String hbaseRefah;

    public String hbaseCurrentah;

    public String hbaseAjlbCol;

    public String hbaseXzquCol;

    public String hbaseEnterTimeCol;

    public String hbaseAddressCol;

    public String hbaseSourceCol;


}
