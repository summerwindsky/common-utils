package zj.sink.hbase.domain;

import java.io.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by lk on 2017/1/10.
 */
public class Document implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private String title;

    private String content;

    private String rowkey;

    private String docDate;

    private long updatetime;
    private long enterUpdatetime;
    //是否内网, 0为内网，1位外网
    private String nw;

    //类型字段：法院文书fy、检察院文书jcy、法院结构化数据fyj、检察院结构化数据jcyj等
    private String lx;

    private String urlId;

    private String old_rowkey;

//    private Set<String> ahs = new HashSet<String>();

    private String currentAH;

    /**
     * 案件类别
     */
    private String ajlb;

    /**
     * 行政区划
     */
    private String xzqh;
    private String source;
    private String address;


    public long getEnterUpdatetime() {
        return enterUpdatetime;
    }

    public void setEnterUpdatetime(long enterUpdatetime) {
        this.enterUpdatetime = enterUpdatetime;
    }

    /**
     * @return the xzqh
     */
    public String getXzqh() {
        return xzqh;
    }

    /**
     * @param xzqh the xzqh to set
     */
    public void setXzqh(String xzqh) {
        this.xzqh = xzqh;
    }

    /**
     * @return the ajlb
     */
    public String getAjlb() {
        return ajlb;
    }

    /**
     * @param ajlb the ajlb to set
     */
    public void setAjlb(String ajlb) {
        this.ajlb = ajlb;
    }

    public Document(String title, String content, String rowkey, String docDate) {
        this.title = title;
        this.content = content;
        this.rowkey = rowkey;
        this.docDate = docDate;
    }

    public Document(String rowkey, String title, String content, long updateTime, String date, String lx, String nw,
                    String urlID, String oldRowKey, String ajlb, String xzqh, String currentAH, Set<String> refAhSet) {
        this.rowkey = rowkey;
        this.title = title;
        this.content = content;
        this.updatetime = updateTime;
        this.docDate = date;
        this.lx = lx;
        this.nw = nw;
        this.urlId = urlID;
        this.old_rowkey = oldRowKey;
        this.currentAH = currentAH;
//        this.ahs = refAhSet;
        this.ajlb = ajlb;
        this.xzqh = xzqh;
    }

    public Document() {
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setUpdatetime(long updatetime) {
        this.updatetime = updatetime;
    }

    public long getUpdatetime() {

        return updatetime;
    }

    public String getTitle() {
        return title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public Document setRowkey(String rowkey) {
        this.rowkey = rowkey;
        return this;
    }

    public String getRowkey() {
        return rowkey;
    }

    public String getDocDate() {
        return docDate;
    }

    public void setDocDate(String docDate) {
        this.docDate = docDate;
    }

    public String getNw() {
        return nw;
    }

    public void setNw(String nw) {
        this.nw = nw;
    }

    public String getLx() {
        return lx;
    }

    public void setLx(String lx) {
        this.lx = lx;
    }

    public String getUrlId() {
        return urlId;
    }

    public void setUrlId(String urlId) {
        this.urlId = urlId;
    }

    public String getOld_rowkey() {
        return old_rowkey;
    }

    public void setOld_rowkey(String old_rowkey) {
        this.old_rowkey = old_rowkey;
    }

//    public Set<String> getAhs() {
//        return ahs;
//    }
//
//    public void setAhs(Set<String> ahs) {
//        this.ahs = ahs;
//    }

    public String getCurrentAH() {
        return currentAH;
    }

    public void setCurrentAH(String currentAH) {
        this.currentAH = currentAH;
    }

    public byte[] toBytes() {
        ByteArrayOutputStream bo = new ByteArrayOutputStream();
        ObjectOutputStream oos = null;
        try {
            oos = new ObjectOutputStream(bo);
            oos.writeObject(this);
            oos.flush();
            oos.close();
            bo.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bo.toByteArray();
    }

    public Document toDocument(byte[] bytes) {
        Document document = null;
        try {
            ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
            ObjectInputStream ois = new ObjectInputStream(bis);
            document = (Document) ois.readObject();
            ois.close();
            bis.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        return document;
    }

    public static void main(String[] args) {
        //        Document document = new Document();
        //        document.setTitle("这是一个title");
        //        document.setContent("这是内容正文");
        //        document.setDocDate("2016-10-12");
        //        document.setRowkey("123");
        //        byte[] bytes = document.toBytes();
        //        Document doc = document.toDocument(bytes);
        //        System.out.print(doc.toString());
        String title = "兰国土资城监(2016)003号.txt";
        System.out.println(title.substring(0, title.lastIndexOf(".txt")));
    }
}
