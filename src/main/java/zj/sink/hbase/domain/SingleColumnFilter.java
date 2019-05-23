package zj.sink.hbase.domain;

/**
 * Created by lk on 2017/1/9.
 */
public class SingleColumnFilter {
    public String family;

    public String qualifier;

    public long value;

    public CompareOpeara compareOp;

    public enum CompareOpeara {
        /** less than */
        LESS,
        /** less than or equal to */
        LESS_OR_EQUAL,
        /** equals */
        EQUAL,
        /** not equal */
        NOT_EQUAL,
        /** greater than or equal to */
        GREATER_OR_EQUAL,
        /** greater than */
        GREATER,
        /** no operation */
        NO_OP,
    }

    //对于缺省过滤列的处理，为true，这样的行将会被过滤掉，为false（默认），这样的行会包含在结果集中
    public boolean filterIfMissing = false;

    //如果设置为false，则除了检查最新版本，还会检查以前的版本。默认值是true，只检查最新版本的值。
    public boolean latestVersionOnly = true;

    public String getFamily() {
        return family;
    }

    public String getQualifier() {
        return qualifier;
    }

    public long getValue() {
        return value;
    }

    public CompareOpeara getCompareOp() {
        return compareOp;
    }

    @Override
    public String toString() {
        return "SingleColumnFilter{" + "family='" + family + '\'' + ", qualifier='" + qualifier + '\'' + ", value='"
                + value + '\'' + ", compareOp=" + compareOp + ", filterIfMissing=" + filterIfMissing
                + ", latestVersionOnly=" + latestVersionOnly + '}';
    }
}
