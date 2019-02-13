package zj.xml;

import net.sf.json.JSONObject;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.dom4j.*;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class XmlUtil {
    private static final Logger logger = LoggerFactory.getLogger(XmlUtil.class);

    /**
     * 删除xml头部的命名空间
     * @param xml
     * @return
     */
    public static String deleteNameSpace(String xml) {
        Document doc = XmlUtil.stringToXml(xml);
        if (doc == null) {
            return "";
        }
        String xmlStr = doc.asXML();
        xmlStr = xmlStr.replaceAll("xmlns=\".*\"", "");
        xmlStr = xmlStr.replaceAll("[\\r\\n]+", "\n");
        xmlStr = xmlStr.replaceAll("(\\n)[\\s]+", "$1");
        xmlStr = xmlStr.replaceAll("[\\s]+(\\n)", "$1");
        xmlStr = xmlStr.replaceAll(">[\\n\\s]+<", "><");
        return xmlStr;
    }

    /**
     * 返回符合该xpath的单一节点的value属性的值，如有多个匹配，则返回第一个
     * 
     * @param elem
     * @param xpath
     * @return
     */
    public static String getSingleValue(Element elem, String xpath) {
        if (elem == null || StringUtils.isBlank(xpath)) {
            return null;
        }
        Node node = elem.selectSingleNode(xpath + XPathConstant.VALUE_PATH);
        if (null == node) {
            return null;
        }
        return node.getStringValue();
    }

    /**
     * 获取节点的value值
     * @param elem
     * @return
     */
    public static String getValue(Element elem, String path) {
        Node node = elem.selectSingleNode(path + XPathConstant.VALUE_PATH);
        if (null == node) {
            return null;
        }
        return node.getStringValue();
    }

    /**
     * 获取节点的value值
     * @param elem
     * @return
     */
    public static String getValue(Element elem) {
        Node node = elem.selectSingleNode("@value");
        if (null == node) {
            return null;
        }
        return node.getStringValue();
    }

    /**
     * 返回符合该xpath的单一节点的value属性的值，如有多个匹配，则返回第一个
     * 
     * @param doc
     * @param xpath
     * @return
     */
    public static String getSingleValue(Document doc, String xpath) {
        if (doc == null || StringUtils.isBlank(xpath)) {
            return null;
        }
        Node node = doc.selectSingleNode(xpath + XPathConstant.VALUE_PATH);
        if (null == node) {
            return null;
        }
        return node.getStringValue();
    }

    public static String getSingleValue(Document doc, List<String> xpathList) {
        if (doc == null || CollectionUtils.isEmpty(xpathList)) {
            return null;
        }
        for (String xpath : xpathList) {
            String value = XmlUtil.getSingleValue(doc, xpath);
            if (null != value) {
                // TODO 不知道结构化数据里日期的格式跟实体识别结果里的是否一致，否则还得写个转换函数
                return value;
            }
        }
        return null;
    }

    /**
     * 获取所有符合xpath的节点的value属性，用“,”分隔返回
     * 
     * @param node
     * @param xpath
     * @return
     */
    public static String getValues(Element node, String xpath) {
        if (node == null || StringUtils.isBlank(xpath)) {
            return null;
        }
        @SuppressWarnings("unchecked")
        List<Attribute> els = node.selectNodes(xpath + XPathConstant.VALUE_PATH);
        if (CollectionUtils.isEmpty(els)) {
            return null;
        } else if (els.size() == 1) {
            return els.get(0).getText();
        } else {
            StringBuilder sb = new StringBuilder();
            for (Attribute el : els) {
                sb.append(el.getText()).append(",");
            }
            return sb.substring(0, sb.length() - 1);
        }
    }

    /**
     * 获取所有符合xpath的节点的value属性，用“,”分隔返回
     * 
     * @param doc
     * @param xpath
     * @return
     */
    public static String getValues(Document doc, String xpath) {
        if (null == doc) {
            return null;
        } else {
            return getValues(doc.getRootElement(), xpath);
        }
    }

    /**
     * 获取所有符合该xpath的元素集合
     * 
     * @param elem
     * @param xpath
     * @return
     */
    public static List<?> getList(Element elem, String xpath) {
        if (elem == null || StringUtils.isBlank(xpath)) {
            return new ArrayList<Object>();
        }
        List<?> lst = elem.selectNodes(xpath);
        return lst;
    }

    public static List<String> getListValues(Element elem, String xpath) {
        List<String> values = new ArrayList<String>();
        if (elem == null || StringUtils.isBlank(xpath)) {
            return values;
        }
        @SuppressWarnings("unchecked")
        List<Attribute> lst = elem.selectNodes(xpath);
        if (!CollectionUtils.isEmpty(lst)) {
            for (Attribute el : lst) {
                values.add(el.getText());
            }
        }
        return values;
    }

    public static List<String> getAttributeListValues(Element elem, String xpath) {
        List<String> values = new ArrayList<String>();
        if (elem == null || StringUtils.isBlank(xpath)) {
            return values;
        }
        @SuppressWarnings("unchecked")
        List<Attribute> lst = elem.selectNodes(xpath + XPathConstant.VALUE_PATH);
        if (!CollectionUtils.isEmpty(lst)) {
            for (Attribute el : lst) {
                values.add(el.getText());
            }
        }
        return values;
    }

    /**
     * 获取副歌该xpath的单一元素，如有多个匹配，则返回第一个
     * 
     * @param elem
     * @param xpath
     * @return
     */
    public static Node getSingleNode(Element elem, String xpath) {
        if (elem == null || StringUtils.isBlank(xpath)) {
            return null;
        }
        return elem.selectSingleNode(xpath);
    }

    /**
     * 获取副歌该xpath的单一元素，如有多个匹配，则返回第一个
     * 
     * @param elem
     * @param xpath
     * @return
     */
    public static Element getSingleElement(Element elem, String xpath) {
        if (elem == null || StringUtils.isBlank(xpath)) {
            return null;
        }
        return (Element) elem.selectSingleNode(xpath);
    }

    public static Document readXml(String inputFile) {
        File file = new File(inputFile);
        return readXml(file);
    }

    public static Document readXml(File file) {
        SAXReader reader = new SAXReader();
        Document document = null;
        try {
            document = reader.read(file);
        } catch (DocumentException e) {
            logger.info("读取xml错误 :" + file.getAbsolutePath());
            return null;
        }
        return document;
    }

    public static void WriteXml(String path, Document doc) {
        if (null == doc) {
            return;
        }
        FileOutputStream fos = null;
        OutputStreamWriter osw = null;
        try {
            fos = new FileOutputStream(path);
            osw = new OutputStreamWriter(fos, "utf-8");
            OutputFormat of = new OutputFormat();
            of.setEncoding("utf-8");
            of.setIndent(true);
            of.setIndent("    ");
            of.setNewlines(true);
            XMLWriter writer = new XMLWriter(osw, of);
            writer.write(doc);
            writer.close();
        } catch (UnsupportedEncodingException e) {
            logger.error("Document字符编码出错：UTF-8");
        } catch (FileNotFoundException e) {
            logger.error("FileNotFound!:" + path);
        } catch (IOException e) {
            logger.error("写xml文件错误！: " + path);
        }

    }

    public static void WriteXml(String path, String xmlStr) {
        Document doc = stringToXml(xmlStr);
        WriteXml(path, doc);
    }

    public static Document stringToXml(String xmlStr) {
        if (StringUtils.isBlank(xmlStr)) {
            return null;
        }
        SAXReader saxReader = new SAXReader();
        Document doc = null;
        try {
            doc = saxReader.read(new ByteArrayInputStream(xmlStr.getBytes("UTF-8")));
            return doc;
        } catch (UnsupportedEncodingException e) {
            logger.error("Document字符编码出错：UTF-8");
            return null;
        } catch (DocumentException e) {
            logger.error("String 转Document出错：" + xmlStr);
            return null;
        }
    }

    public static boolean removeAttribute(Element e, String xpath, Object obj) {
        List<String> lst = new ArrayList<String>();
        if (obj instanceof String) {
            lst.add((String) obj);
        } else if (obj instanceof List) {
            List<?> tList = (List<?>) obj;
            for (Object tObj : tList) {
                if (tObj instanceof String) {
                    lst.add((String) tObj);
                }
            }
        }

        List<?> loopList = e.selectNodes(xpath);
        if (CollectionUtils.isNotEmpty(loopList)) {
            int elementSize = loopList.size();
            for (int i = 0; i < elementSize; i++) {
                Element tarNode = (Element) loopList.get(i);
                for (String str : lst) {
                    Attribute attr = tarNode.attribute(str);
                    if (attr != null) {
                        tarNode.remove(attr);
                    }
                }
            }
        }
        return true;
    }

    public static boolean cleanOValue(Element e) {
        String ovalueXpath = ".//*[@oValue]";
        String ovalueAttr = "oValue";
        return removeAttribute(e, ovalueXpath, ovalueAttr);
    }

    public static boolean cleanNameCN(Element e) {
        String xpath = "//*[@nameCN]";
        String attr = "nameCN";
        return removeAttribute(e, xpath, attr);
    }

    public static JSONObject cleanNameCN(JSONObject jObj) {
        if (jObj == null) {
            return jObj;
        }
        String jStr = jObj.toString();
        return JSONObject.fromObject(cleanNameCN(jStr));
    }

    public static String cleanNameCN(String json) {
        if (StringUtils.isBlank(json)) {
            return null;
        }
        String nameCN = "\"@nameCN\"[\\s]*:[\\s]*\"[^\"]*\",?";
        String jStr = json.replaceAll(nameCN, "");
        ;
        return jStr;
    }

    public static boolean cleanOValueAndNameCN(Element e) {
        // 删除所有ovalue属性以及nameCN属性
        String xpath = "//*[@oValue or @nameCN]";
        List<String> attList = new ArrayList<String>();
        attList.add("oValue");
        attList.add("nameCN");
        return removeAttribute(e, xpath, attList);
    }

    /**
     * @param e
     * @param xpath
     * @return
     */
    public static boolean deleteNode(Element e, String xpath) {
        List<?> loopList = e.selectNodes(xpath);
        while (loopList.size() > 0) {
            Iterator<?> tarIt = loopList.iterator();
            while (tarIt.hasNext()) {
                Element tarNode = (Element) tarIt.next();
                tarNode.detach();
            }
            loopList = e.selectNodes(xpath);
        }
        return true;
    }

    public static boolean deleteSingleNode(Element e, String xpath) {
        List<?> loopList = e.selectNodes(xpath);
        if (loopList.size() > 0) {
            Iterator<?> tarIt = loopList.iterator();
            while (tarIt.hasNext()) {
                Element tarNode = (Element) tarIt.next();
                tarNode.detach();
            }
        }
        return true;
    }

    /**
     * 
     * @param document
     * @param xpath
     * @return
     */
    public static boolean deleteNode(Document document, String xpath) {
        List<?> loopList = document.selectNodes(xpath);
        while (loopList.size() > 0) {
            Iterator<?> tarIt = loopList.iterator();
            while (tarIt.hasNext()) {
                Element tarNode = (Element) tarIt.next();
                tarNode.detach();
            }
            loopList = document.selectNodes(xpath);
        }
        return true;
    }

    public static boolean cleanNonNode(Element e) {
        // 清除没有子节点且本身没有value属性或者value属性为空的节点
        String xpath = "//*[not(*)][not(@value) or @value='']";
        return deleteNode(e, xpath);
    }

    /**
     * 去掉结构化数据里_C结尾的节点，其它节点添加子节点@value
     * 
     */
    public static void dealStructData(Element e) {
        if (null == e) {
            return;
        }
        String text = e.getText();
        if (null != text && StringUtils.isNotBlank(text)) {
            e.addAttribute("value", e.getText());
            e.setText("");
        }
        @SuppressWarnings("unchecked")
        List<Element> elements = e.elements();
        if (null != elements) {
            for (Element child : elements) {
                if (child.getName().endsWith("_C")) {
                    e.remove(child);
                } else {
                    dealStructData(child);
                }
            }
        }
    }

    public static String dealAh(String ah) {
        if (null == ah) {
            return null;
        }
        ah = ah.replaceAll("[\\(\\)\\[\\]<>（）【】《》<>＜＞〔〕［］]", "");
        ah = ah.replaceAll("(?<!\\d)0+(?=[^0])", "");
        return ah;
    }
}
