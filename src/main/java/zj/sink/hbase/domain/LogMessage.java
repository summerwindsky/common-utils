package zj.sink.hbase.domain;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 *  日志Pattern：
 *  	log4j.properties ： 
 *  		org.apache.log4j.Logger：   %d{ISO8601} [%l-%M]-[%p] %t %m%n
 *  	logback.xml ： 
 *  		org.slf4j.Logger：     %date [%logger:%L]-[%level] %thread %msg%n
 *  
 * 	sysName 系统名称
 *  sysModule 系统模块
 *	relatedBH 相关编号
 *  xzqh 行政区划
 *  action 日志行为
 *  action_status 日志行为的结果
 *  info 日志内容
 *  relatedDept 涉及单位
 *  
 *  map 用于存放上述没有涉及内容，注意：map中暂不支持存放json数据
 *  
 */
public class LogMessage {

    private ConcurrentMap<String, String> map = new ConcurrentHashMap<String, String>();

    private String sysName = "睿核";

    private String sysModule = "kafka数据入hbase";

    private String relatedBH;

    private String xzqh;

    private String action;

    private String action_status;

    private String info;

    private String relatedDept;

    public LogMessage() {
    }

    public String getSysName() {
        return sysName;
    }

    public LogMessage setSysName(String sysName) {
        this.sysName = sysName;
        return this;
    }

    public String getSysModule() {
        return sysModule;
    }

    public LogMessage setSysModule(String sysModule) {
        this.sysModule = sysModule;
        return this;
    }

    public String getRelatedBH() {
        return relatedBH;
    }

    public LogMessage setRelatedBH(String relatedBH) {
        this.relatedBH = relatedBH;
        return this;
    }

    public String getXzqh() {
        return xzqh;
    }

    public LogMessage setXzqh(String xzqh) {
        this.xzqh = xzqh;
        return this;
    }

    public String getAction() {
        return action;
    }

    public LogMessage setAction(String action) {
        this.action = action;
        return this;
    }

    public String getAction_status() {
        return action_status;
    }

    public LogMessage setAction_status(String action_status) {
        this.action_status = action_status;
        return this;
    }

    public String getInfo() {
        return info;
    }

    public LogMessage setInfo(String info) {
        this.info = info;
        return this;
    }

    public String getRelatedDept() {
        return relatedDept;
    }

    public LogMessage setRelatedDept(String relatedDept) {
        this.relatedDept = relatedDept;
        return this;
    }

    public ConcurrentMap<String, String> getMap() {
        return map;
    }

    public LogMessage setMap(ConcurrentMap<String, String> map) {
        this.map = map;
        return this;
    }

    @Override
    public String toString() {
        return "{sysName###" + sysName + "@@@sysModule###" + sysModule + "@@@relatedBH###" + relatedBH + "@@@xzqh###"
                + xzqh + "@@@action###" + action + "@@@action_status###" + action_status + "@@@info###" + info
                + "@@@map###" + map + "@@@relatedDept###" + relatedDept + "}";
    }
}
