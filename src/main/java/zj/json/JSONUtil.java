//package zj.json;
//
//import jline.internal.InputStreamReader;
//import net.sf.json.JSONObject;
//import org.apache.commons.lang.StringUtils;
//import org.codehaus.jackson.JsonGenerationException;
//import org.codehaus.jackson.map.JsonMappingException;
//import org.codehaus.jackson.map.ObjectMapper;
//
//import java.io.BufferedReader;
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.IOException;
//import java.util.regex.Pattern;
//
///**
// *
// * Title: JSONUtil
// * Description: json工具类，目前唯一提供的功能是删除xml转换json字符串后的nameCN属性
// * Company: 北京华宇元典信息服务有限公司
// *
// * @author maxiao
// * @version 1.0
// * @date 2017-4-26 下午1:51:44
// *
// */
//public class JSONUtil {
//
//    //预编译的正则表达式
//    private static Pattern pp = Pattern.compile(
//        "(?:(?:\"\\w*?\"\\s*?:\\s*?\"\"\\s*?|\"\\w*?\"\\s*?:\\s*?\\[\\s*?\\]\\s*?)|(?:\"@nameCN\"\\s*:\\s*\"[^\"]*\"\\s*?))(?:,|(?=\\}))");
//
//    private static Pattern pp2 = Pattern.compile(",+\\}");
//
//    public static void saveToFile(JSONObject json, String outputFile) {
//        ObjectMapper mapper = new ObjectMapper();
//        try {
//            File file = new File(outputFile.substring(0, outputFile.lastIndexOf("\\")));
//            if (!file.exists()) {
//                file.mkdirs();
//            }
//            mapper.writer().withDefaultPrettyPrinter().writeValue(new File(outputFile), json);
//        } catch (JsonGenerationException e) {
//            e.printStackTrace();
//        } catch (JsonMappingException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public static JSONObject readJSON(String inputFile) {
//        BufferedReader bw = null;
//        try {
//            bw = new BufferedReader(new InputStreamReader(new FileInputStream(new File(inputFile))));
//            StringBuilder sb = new StringBuilder();
//            String line = null;
//            while ((line = bw.readLine()) != null) {
//                sb.append(line);
//                sb.append("\n");
//            }
//            JSONObject json = JSONObject.fromObject(sb.toString());
//            return json;
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
//
//    /**
//     * 删除nameCN属性
//     * @param jObj    传入的jsonobject
//     * @return        返回的jsonobject
//     */
//    @Deprecated
//    public static JSONObject cleanNameCN(JSONObject jObj) {
//        if (jObj == null) {
//            return jObj;
//        }
//        String jStr = jObj.toString();
//        return JSONObject.fromObject(cleanNameCN(jStr));
//    }
//
//    /**
//     * 清理nameCN和空字符串,空json数组的kv
//     * @param jObj
//     * @return
//     */
//    public static JSONObject cleanNameCNAndEmptyKV(JSONObject jObj) {
//        if (jObj == null) {
//            return jObj;
//        }
//        String jStr = jObj.toString();
//        jStr = pp.matcher(jStr).replaceAll("");
//        jStr = pp2.matcher(jStr).replaceAll("}");
//        JSONObject result = JSONObject.fromObject(jStr);
//        return result;
//    }
//
//    /**
//     * 清理空字符串值,空json数组的kv
//     * @param jObj
//     * @return
//     */
//    @Deprecated
//    public static JSONObject cleanEmptyKV(JSONObject jObj) {
//        if (jObj == null) {
//            return jObj;
//        }
//        String jStr = jObj.toString();
//        String emptyKV = "\"\\w*?\"\\s*?:\\s*?\"\"\\s*?,|\"\\w*?\"\\s*?:\\s*?\\[\\s*?\\]\\s*?,";
//        String emptyKVLast = ",\\s*?\"\\w*?\"\\s*?:\\s*?\"\"(?!,)|,\\s*?\"\\w*?\"\\s*?:\\s*?\\[\\s*?\\](?!,)";
//        jStr = jStr.replaceAll(emptyKV, "");
//        jStr = jStr.replaceAll(emptyKVLast, "");
//        return JSONObject.fromObject(jStr);
//    }
//
//    /**
//     * 利用正则表达式替换所有的nameCN属性对
//     * @param json
//     * @return
//     */
//    @Deprecated
//    public static String cleanNameCN(String json) {
//        if (StringUtils.isBlank(json)) {
//            return null;
//        }
//        String nameCN = "\"@nameCN\"[\\s]*:[\\s]*\"[^\"]*\",?";
//        String jStr = json.replaceAll(nameCN, "");
//        return jStr;
//    }
//
//    public static void main(String[] args) {
//        JSONUtil.saveToFile(JSONUtil.cleanEmptyKV(JSONUtil.readJSON("E:\\gitWorkSpace\\importCase\\设计文档\\json2.txt")),
//            "E:\\gitWorkSpace\\importCase\\设计文档\\json2.txt");
//    }
//}
