package zj.hyyd;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.thunisoft.hyyd.ft.entity.Flfg;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Title:  LawUtil
 * Description: TODO
 *
 * @author ljp
 * @version 1.0
 * @date 2018/12/3 15:00
 */
@Slf4j
public class LawFtUtil {


    public static void main(String[] args) {
        // FIXME getLawDetail 获取法宝中的法规
        // FIXME getFv 将法宝法规处理成标准结构
        // FIXME flftUtil 将具体法条名称识别 获取条款项目
        // FIXME getLawContent(String mc, String t, String tz) 获取具体法条内容 （精确到条）
        // FIXME getLawContent(String mc, String t, String tz, String k, String x) 获取具体法条内容（精确到款项目）
//        System.out.println(getFv(getLawDetail("《最高人民法院关于实施修订后的&lt;关于常见犯罪的量刑指导意见&gt;的通知》第四条第一款")));
//        System.out.println(getFv(getLawDetail("《公安部关于毒品案件立案标准的通知》")));
//        System.out.println(getFv(getLawDetail("《最高人民法院、最高人民检察院<关于办理组织、强迫、引诱、容留、介绍卖淫刑事案件>适用法律若干问题的解释》")));
//        FtUtil flftUtil = new FtUtil("《中华人民共和国刑法》第二百九十三条第一款第（四）项");
//        FtUtil flftUtil = new FtUtil("《人民法院量刑指导意见(试行)》第四条第一款第（二）项");
//        FtUtil flftUtil = new FtUtil("《最高人民法院关于实施修订后的&lt;关于常见犯罪的量刑指导意见&gt;的通知》第四条第一款");
//        FtUtil flftUtil = new FtUtil("《最高人民法院关于实施修订后的<关于常见犯罪的量刑指导意见>的通知》第四条第一款");
//        FtUtil flftUtil = new FtUtil("《最高人民法院关于审理交通肇事刑事案件具体应用法律若干问题的解释》第二条第一款");
//        FtUtil flftUtil1 = new FtUtil("《最高人民法院关于审理交通肇事刑事案件具体应用法律若干问题的解释》第二条第一款第（二）项");

        FtUtil flftUtil1 = new FtUtil("《最高人民法院关于实施修订后的<关于常见犯罪的量刑指导意见>的通知》第四条第十五款第三项");
//        FtUtil flftUtil = new FtUtil("《最高人民法院关于实施修订后的<关于常见犯罪的量刑指导意见>的通知》第四条第十五款第三项第一目");
        FtUtil flftUtil = new FtUtil("《最高人民法院、最高人民检察院、公安部关于印发<办理毒品犯罪案件适用法律若干问题的意见>的通知》第九条");

//        FtUtil flftUtil2 = new FtUtil("《最高人民法院关于常见犯罪的量刑指导意见(二)(试行)》第一条第三项");
//        FlftUtil flftUtil2 = new FlftUtil("《最高人民法院关于常见犯罪的量刑指导意见(二)(试行)》第一条第三项");
        FlftUtil flftUtil2 = new FlftUtil("《最高人民法院关于常见犯罪的量刑指导意见(二)(试行)》第一条第一款第三项");
//        FtUtil flftUtil2 = new FtUtil("《中华人民共和国刑法》第一百三十三条第一款");

//        FtUtil flftUtil1 = new FtUtil("《最高人民法院关于实施修订后的<关于常见犯罪的量刑指导意见>的通知(2017)》第四条第十五款第三项");
//        FtUtil flftUtil = new FtUtil("《最高人民法院关于实施修订后的<关于常见犯罪的量刑指导意见>的通知(2017)》第四条第十五款第三项第三目");
//        FtUtil flftUtil = new FtUtil("《中华人民共和国刑法》第二百一十七条第（一）项");
        //FIXME 认为法规没有款就是具体到条的查询
//        if (flftUtil.getKuan() != null) {
            System.out.println(getLawContent(flftUtil.getMc(), flftUtil.getTiao(), flftUtil.getTiao_zhi(),flftUtil.getKuan(), flftUtil.getXiang()));
            System.out.println(getLawContent(flftUtil1.getMc(), flftUtil1.getTiao(), flftUtil1.getTiao_zhi(), flftUtil1.getKuan(),flftUtil1.getXiang()));
//            System.out.println(getLawContent(flftUtil2.getMc(), flftUtil2.getTiao(), flftUtil2.getTiao_zhi(), flftUtil2.getKuan(),flftUtil2.getXiang()));
            System.out.println(getLawContent(flftUtil2.getMc(), flftUtil2.getT(), null, flftUtil2.getK(),flftUtil2.getX()));
//        }
    }

    public static String getLawContent(String mc, String t, String tz) {
        return getLawContent(mc, t, tz, null, null);
    }

    public static String getLawContent(String mc, String t, String tz, String k, String x) {
        String url = "http://210.74.3.239:6030/Db/GetSingleRecordLawContent?title=" + escapeSpecialCharacter(mc);
        String tiao = "";
        if (StringUtils.isNotBlank(t)) {
            tiao = "&tiao=" + t;
            if (tz != null) {
                int zhi = Integer.parseInt(tz);
                if (zhi > 0 && zhi <= 6) {
                    tiao = tiao + String.valueOf((char) (zhi + 96)) + ":";
                }
            }
        }
        String kuan = StringUtils.isBlank(k) ? "" : "&kuan=" + k;
        String xiang = StringUtils.isBlank(x) ? "" : "&xiang=" + x;
        url += tiao + kuan + xiang;
        try {
            ResponseEntity<String> entity = new RestTemplate().getForEntity(url, String.class);
            JSONObject jsb = JSON.parseObject(entity.getBody());
            if (jsb.getString("Data").startsWith("没有找到")) {
                return null;
            }
            return jsb.getString("Data");
            //FIXME 替换回车换行
            // return jsb.getString("Data").replaceAll("\\\\r\\\\n", "<br/>");
        } catch (Exception e) {
            log.error("获取法律法条出错：{}", url);
            log.error("获取法律法条出错：", e);
            return null;
        }
    }

    /**
     * 转义查询文本中的特殊字符（包括空格、全角空格、+、/、\、%、#、&）
     * 注：%处理在第一个
     *
     * @param text 查询文本
     * @return
     */
    private static String escapeSpecialCharacter(String text) {
        return text.replaceAll("%", "%25").replaceAll(" ", "%20").replaceAll("　", "%E3%80%80").replaceAll("\\+", "%2B").replaceAll("/", "%2F")
                .replaceAll("\\?", " %3F").replaceAll("#", "%23").replaceAll("&", "%26");
    }

    private static final Pattern P_ARAB = Pattern.compile("\\d+");
    private static Pattern PATTERN = Pattern.compile("(第.*?条(之[一二三四五六七八九十]*?)?(.*?款)?(.*?项)?(.*?目)?)$");
    private static Pattern PATTERN_TIAO = Pattern.compile("第.*?条");
    private static Pattern PATTERN_TIAO_ZHI = Pattern.compile("之\\d+");
    private static Pattern PATTERN_KUAN = Pattern.compile("第.*?款");
    private static Pattern PATTERN_XIANG = Pattern.compile("第.*?项");
    private static Pattern PATTERN_MU = Pattern.compile("第.*?目");

    @Getter
    @Setter
    @ToString
    public static class FtUtil {

        // 原始法律法条
        private String original;

        // 法条名称
        private String mc;

        // 法条_条：数字
        private String tiao;

        //法条_条：之n
        private String tiao_zhi;

        // 法条_款：数字
        private String kuan;

        // 法条_项：数字
        private String xiang;

        // 法条_目：数字
        private String mu;

        public FtUtil(String original) {
            this.original = original;
            Matcher matcher = PATTERN.matcher(original);
            if (matcher.find()) {
                this.mc = original.substring(0, matcher.start());
                original = fgZH2Arab(matcher.group());
                Matcher tiao = PATTERN_TIAO.matcher(original);
                if (tiao.find()) {
                    Matcher numTiao = P_ARAB.matcher(tiao.group());
                    if (numTiao.find()) {
                        this.tiao = numTiao.group() + "";
                        original = original.substring(numTiao.end());
                        Matcher tiao_zhi = PATTERN_TIAO_ZHI.matcher(original);
                        if (tiao_zhi.find()) {
                            Matcher numTiaoZhi = P_ARAB.matcher(tiao_zhi.group());
                            if (numTiaoZhi.find()) {
                                this.tiao_zhi = numTiaoZhi.group() + "";
                                original = original.substring(numTiaoZhi.end());
                            }
                        }
                        Matcher kuan = PATTERN_KUAN.matcher(original);
                        if (kuan.find()) {
                            Matcher numKuan = P_ARAB.matcher(kuan.group());
                            if (numKuan.find()) {
                                this.kuan = numKuan.group() + "";
                                original = original.substring(numKuan.end());
                                Matcher xiang = PATTERN_XIANG.matcher(original);
                                if (xiang.find()) {
                                    Matcher numXiang = P_ARAB.matcher(xiang.group());
                                    if (numXiang.find()) {
                                        this.xiang = numXiang.group() + "";
                                        original = original.substring(numXiang.end());
                                        Matcher mu = PATTERN_MU.matcher(original);
                                        if (mu.find()) {
                                            Matcher numMu = P_ARAB.matcher(mu.group());
                                            if (numMu.find()) {
                                                this.mu = numMu.group() + "";
                                            }
                                        }
                                    }
                                }

                            }
                        }
                    }
                }

            } else {
                this.mc = original;
            }

        }
    }

    @Getter
    @Setter
    @ToString
    static class Fv {
        private String Gid;//唯一标识id
        private String ImplementDate;//实施日期
        private String IssueDate;//发布日期
        private String Title;//只存title
        private String Category;//法规类别(子类别)
        private String DocumentNO;//发文字号
        private String IssueDepartment;//发布部门
        private String TimelinessDic;//时效性
        private String Library;//效力级别主类别库名
        private String Effectiveness;//效力级别主类别(中文）
        private String EffectiveShow;//效力级别子类别(中文）
    }

    private static final Pattern DEAL = Pattern.compile("[《<（)(）>”“、》]");


    private static final String CHL = "CHL";

    /**
     * 提取法律信息中的属性
     *
     * @param fvJson 法律信息
     * @return
     */
    @SuppressWarnings("unchecked")
    public static Flfg getFv(JSONObject fvJson) {
        if (fvJson == null) {
            return null;
        }
//        System.out.println(fvJson);
        Flfg fv = new Flfg();
//        Fv fv = new Fv();
        String tempgid = fvJson.getString("Gid");
        fv.setGid(tempgid);
        // 处理标题
        String temptitle = fvJson.getString("Title");
        // 标题
        fv.setTitle(temptitle);
        // 实施日期
        String tempdate = fvJson.getString("ImplementDate");
        fv.setImplementDate(tempdate);

        // 发布日期
        String tempIssueDate = fvJson.getString("IssueDate");
        fv.setIssueDate(tempIssueDate);

        // 发布部门
        Map<String, String> issueDepartment = (Map<String, String>) fvJson.get("IssueDepartment");
        fv.setIssueDepartment(getIssueDepartment(issueDepartment));

        // 时效性
        Map<String, String> timelinessDic = (Map<String, String>) fvJson.get("TimelinessDic");
        fv.setTimelinessDic(getFirst(timelinessDic));

        // 效力级别
        Map<String, String> tempMap2 = (Map<String, String>) fvJson.get("EffectivenessDic");
        if (MapUtils.isNotEmpty(tempMap2)) {
            Set<String> keySet = tempMap2.keySet();
            for (String str : keySet) {
                String effectiveness = "";
                String effectivenessDic = "";
                if (CHL_MAP.containsKey(str)) {
                    effectivenessDic = CHL;
                    effectiveness = CHL_MAP.get(str);
                    fv.setLibrary(effectivenessDic);
                    fv.setEffectiveness(effectiveness);
                    break;
                }
            }
        }
        // 发文字号
        String documentNO = fvJson.getString("DocumentNO");
        if (StringUtils.isNotEmpty(documentNO)) {
            fv.setDocumentNO(documentNO);
        }
        Map<String, String> effectiveShow = (Map<String, String>) fvJson.get("Effectiveness");
        if (MapUtils.isNotEmpty(effectiveShow)) {
            fv.setEffectiveShow(effectiveShow.get("Value"));
        }
        // 法规类别
        Map<String, String> category = (Map<String, String>) fvJson.get("Category");
        fv.setCategory(getTreeChildren(category));
        return fv;
    }

    /**
     * 中央法律法规效力级别第二层级
     */
    public static final Map<String, String> CHL_MAP = new HashMap<>();

    static {
        CHL_MAP.put("xa01", "法律");
        CHL_MAP.put("xc02", "行政法规");
        CHL_MAP.put("xe03", "部门规章");
        CHL_MAP.put("xg04", "司法解释");
        CHL_MAP.put("xi05", "团体规定");
        CHL_MAP.put("xk06", "行业规定");
        CHL_MAP.put("xq09", "军事法规");
    }

    private static String getFirst(Map<String, String> tempMap) {
        String value = "";
        if (MapUtils.isNotEmpty(tempMap)) {
            value = tempMap.entrySet().iterator().next().getValue();
        }
        return value;
    }

    /**
     * @return
     */
    private static String getTreeChildren(Map<String, String> tempMap) {
        String value = "";
        String key = "";
        if (MapUtils.isNotEmpty(tempMap)) {
            for (Map.Entry<String, String> er : tempMap.entrySet()) {
                if (key.length() < er.getKey().length()) {
                    value = er.getValue();
                    key = er.getKey();
                }
            }
        }
        return value;
    }

    /**
     * 获得发布部门
     *
     * @param tempMap
     * @return
     */
    private static String getIssueDepartment(Map<String, String> tempMap) {
        StringBuilder value = new StringBuilder();
        Map<String, Map<String, String>> reMap = new TreeMap<>();
        if (MapUtils.isNotEmpty(tempMap)) {
            for (Map.Entry<String, String> entry : tempMap.entrySet()) {
                String tempMapKey = entry.getKey();
                String tempMapValue = entry.getValue();
                String tempKey = tempMapKey.substring(0, 1);
                Boolean replace = false;
                for (Map.Entry<String, Map<String, String>> entry1 : reMap.entrySet()) {
                    String key = entry1.getKey();
                    if (tempKey.equals(key)) {
                        Map<String, String> map = entry1.getValue();
                        String reMapKey = map.entrySet().iterator().next().getKey();
                        String strLenMax = tempMapKey;
                        String strLenMin = reMapKey;
                        if (tempMapKey.length() < reMapKey.length()) {
                            strLenMax = reMapKey;
                            strLenMin = tempMapKey;
                        }
                        if (strLenMax.indexOf(strLenMin) == 0 && !StringUtils.equals(strLenMax, reMapKey)) {
                            replace = true;
                            break;
                        }
                    }
                }
                if (MapUtils.isEmpty(reMap) || !reMap.containsKey(tempKey) || replace) {
                    Map<String, String> map = new HashMap<>();
                    map.put(tempMapKey, tempMapValue);
                    reMap.put(tempKey, map);
                }
            }
            for (Map.Entry<String, Map<String, String>> entry1 : reMap.entrySet()) {
                Map<String, String> map = entry1.getValue();
                String reMapValue = map.entrySet().iterator().next().getValue();
                value.append("、").append(reMapValue);
            }
        }
        return value.toString().replaceAll("^、", "");
    }

    /**
     * 获得法规详情
     *
     * @param line
     * @return
     */
    public static JSONObject getLawDetail(String line) {
        String newLine;
        //去除书名号方便搜索
        if (line.startsWith("《")) {
            newLine = line.substring(1, line.length() - 1);
        } else {
            newLine = line;
        }
        //调用法宝查询列表
        String sb = "http://210.74.3.239:6030/Db/LibraryRecordList?Library=CHL&PageSize=100&PageIndex=0&Model.Title=" +
                newLine;
        ResponseEntity<String> entity = new RestTemplate().getForEntity(sb, String.class);
        JSONObject da = JSON.parseObject(entity.getBody());
        JSONObject Data = da.getJSONObject("Data");
        if (MapUtils.isNotEmpty(Data)) {
            JSONArray jsa = Data.getJSONArray("Collection");
            if (CollectionUtils.isNotEmpty(jsa)) {
                //FIXME 返回结果只有一个，默认找到对应
                if (jsa.size() == 1) {
                    return jsa.getJSONObject(0);
                } else {
                    String newLine2 = DEAL.matcher(line).replaceAll("");
                    for (int i = 0; i < jsa.size(); i++) {
                        String title = jsa.getJSONObject(i).getString("Title");
                        if (title.equals(newLine)) {
                            return jsa.getJSONObject(i);
                        }
                        String temp = DEAL.matcher(title).replaceAll("");
                        if (temp.equals(newLine2)) {
                            return jsa.getJSONObject(i);
                        }
                        if (temp.startsWith(newLine2)) {
                            Map<String, String> timelinessDic = (Map<String, String>) jsa.getJSONObject(i).get("TimelinessDic");
                            if ("现行有效".equals(getFirst(timelinessDic))) {
                                return jsa.getJSONObject(i);
                            }
                        }

                    }
                }
            }
        }
        return null;
    }


    /**
     * 数字转中文
     *
     * @param tkxm
     * @return
     */
    public static String fgArab2ZH(String tkxm) {
        try {
            StringBuffer sb = new StringBuffer(tkxm.length());
            Matcher m2 = P_ARAB.matcher(tkxm);
            while (m2.find()) {
                String arab = m2.group();
                m2.appendReplacement(sb, tansForm(arab));
            }
            m2.appendTail(sb);
            return sb.toString();
        } catch (Exception e) {
            return tkxm;
        }
    }

    /**
     * 数字转中文 中文转数字
     *
     * @param tkxm
     * @return
     */
    public static String fgZH2Arab(String tkxm) {
        //只要有数字就不转换中文
        if (P_ARAB.matcher(tkxm).find()) {
            return tkxm;
        }
        try {
            return convertString(tkxm);
        } catch (Exception e) {
            return tkxm;
        }
    }

    public static String convertString(String string) {
        StringBuilder builder = new StringBuilder();
        List<NumberEnum> tempList = new ArrayList<>();
        // 这个个标识位，用来判断当前数字 只有1-9
        boolean isSimple = true;
        for (int i = 0; i < string.length(); i++) {
            NumberEnum numberEnum = NumberEnum.getByChar(string.charAt(i));
            if (numberEnum == null && tempList.size() != 0) {
                if (isSimple) {
                    builder.append(convert2Simple(tempList));
                } else if (tempList.size() == 1 && tempList.get(0).type > 2) {
                    builder.append(string.charAt(i - 1));
                } else {
                    builder.append(convert2Number(tempList));
                }
                tempList = new ArrayList<>();
            }
            if (numberEnum == null) {
                isSimple = true;
                builder.append(string.charAt(i));
                continue;
            }
            if (numberEnum.type > 1 && isSimple) {
                isSimple = false;
                if (tempList.size() >= 2) {
                    builder.append(convert2Simple(tempList.subList(0, tempList.size() - 1)));
                    NumberEnum temp = tempList.get(tempList.size() - 1);
                    if (NumberEnum.ZERO.equals(temp)) {
                        temp = NumberEnum.OPT_ZERO;
                    }
                    tempList = new ArrayList<>();
                    tempList.add(temp);
                }
            }
            tempList.add(numberEnum);
            if (i == string.length() - 1) {
                if (isSimple) {
                    builder.append(convert2Simple(tempList));
                } else if (tempList.size() == 1 && tempList.get(0).type > 2) {
                    builder.append(string.charAt(i));
                } else {
                    builder.append(convert2Number(tempList));
                }
            }
        }
        return builder.toString();
    }

    private static String convert2Simple(List<NumberEnum> tempList) {
        StringBuilder builder = new StringBuilder();
        for (NumberEnum numberEnum : tempList) {
            builder.append(numberEnum.value);
        }
        return builder.toString();
    }

    private static String convert2Number(List<NumberEnum> numberList) {
        boolean optZero = false;
        if (numberList.size() >= 1 && numberList.get(0).equals(NumberEnum.OPT_ZERO)) {
            optZero = true;
            numberList.set(0, NumberEnum.ONE);
        }
        List<NumberEnum> tempList = new ArrayList<>();
        Long result = 0L;
        for (int i = 0; i < numberList.size(); i++) {
            NumberEnum numberEnum = numberList.get(i);
            if (numberEnum.type == 4) {
                if (result >= NumberEnum.TEN_THOUSAND.value) {
                    result = (result + convert2BasicNum(tempList)) * numberEnum.value;
                } else {
                    result = result + convert2BasicNum(tempList) * numberEnum.value;
                }
                tempList = new ArrayList<>();
            } else {
                tempList.add(numberList.get(i));
            }
            if (i == numberList.size() - 1) {
                result = result + convert2BasicNum(tempList);
            }
        }
        return optZero ? result.toString().replaceFirst("1", "0") : result.toString();
    }

    private static Long convert2BasicNum(List<NumberEnum> numberList) {
        // 默认设置
        NumberEnum last = NumberEnum.ONE;
        Long result = 0L;
        for (int i = 0; i < numberList.size(); i++) {
            NumberEnum numberEnum = numberList.get(i);
            if (numberEnum.type == 2 || numberEnum.type == 3) {
                if (last == NumberEnum.ZERO) {
                    last = NumberEnum.ONE;
                }
                result = result + last.value * numberEnum.value;
            }
            if (i == numberList.size() - 1 && numberEnum.type == 1) {
                if (last == NumberEnum.ZERO || numberList.size() == 1) {
                    result = result + numberEnum.value;
                } else {
                    result = result + (numberEnum.value * last.value) / 10;
                }
            }
            last = numberEnum;
        }
        return result;
    }


    enum NumberEnum {
        // OPT_ZERO 用于标识前段末位为0
        OPT_ZERO(null, null, null), ZERO("零〇", 0L, 1), ONE("一壹", 1L, 1), TWO("二两贰", 2L, 1), THREE("三叁", 3L, 1), FOUR("四肆", 4L, 1), FIVE("五伍", 5L, 1), SIX("六陆",
                6L, 1), SEVEN("七柒", 7L, 1), EIGHT("八捌", 8L, 1), NINE("九玖", 9L, 1), TEN("十拾", 10L,
                2), HUNDRED("百佰", 100L, 3), THOUSAND("千仟", 1000L, 3), TEN_THOUSAND("万萬", 10000L, 4), HUNDRED_MILLION("亿億", 100000000L, 4);

        String key;
        Long value;
        Integer type;
        private static Map<Character, NumberEnum> map;

        public static NumberEnum getByChar(Character character) {
            if (map == null) {
                map = new HashMap<>();
                for (NumberEnum number : NumberEnum.values()) {
                    if (number.equals(OPT_ZERO)) {
                        continue;
                    }
                    for (int i = 0; i < number.key.length(); i++) {
                        map.put(number.key.charAt(i), number);
                    }
                    if (number.type == 1) {
                        map.put(number.value.toString().charAt(0), number);
                    }
                }
            }
            return map.get(character);
        }

        NumberEnum(String key, Long value, Integer type) {
            this.key = key;
            this.value = value;
            this.type = type;
        }
    }

    private static String tansForm(String number) {
        int length = number.length();
        String digit = "";
        String num = "";
        StringBuilder zh = new StringBuilder();
        //“零”的间隔输出
        boolean zerointerval = true;
        //int comma=0;//控制逗号输出;
        for (int i = 0; i < number.length(); i++) {
            if (length == 2) {
                digit = "十";
            } else if (length == 3) {
                digit = "百";
            } else if (length == 4) {
                digit = "千";
            } else if (length == 5) {
                digit = "万";
            } else if (length == 6) {
                digit = "十万";
            } else if (length == 7) {
                digit = "百万";
            } else if (length == 8) {
                digit = "千万";
            } else if (length == 9) {
                digit = "亿";
            } else if (length == 10) {
                digit = "十亿";
            } else if (length == 11) {
                digit = "百亿";
            } else if (length == 12) {
                digit = "千亿";
            } else if (length == 13) {
                digit = "万亿";
            } else if (length == 1) {
                digit = "";
            }

            String s = (String) (number.subSequence(i, i + 1));


            switch (s) {
                case "1":
                    num = "一";
                    break;
                case "2":
                    num = "二";
                    break;
                case "3":
                    num = "三";
                    break;
                case "4":
                    num = "四";
                    break;
                case "5":
                    num = "五";
                    break;
                case "6":
                    num = "六";
                    break;
                case "7":
                    num = "七";
                    break;
                case "8":
                    num = "八";
                    break;
                case "9":
                    num = "九";
                    break;
                case "0":
                    //如果可以输出“零”
                    if (zerointerval) {
                        num = "零";
                        digit = "";
                        //当输出第一个零后，后面的数为0也不会输出零，如1003不是一千零零三
                        zerointerval = false;
                    } else {
                        num = "";
                        digit = "";
                    }
                    break;
                default:
                    break;
            }

            length--;
            //当输出其他数字后，零又再可以被输出
            if (!"".equals(digit)) {
                zerointerval = true;
            }
            //拼接中文,防止输出更多的亿和万
            if (digit.contains("亿")) {
                zh = new StringBuilder(zh.toString().replace("亿", ""));
            }
            if (digit.contains("万")) {
                zh = new StringBuilder(zh.toString().replace("万亿", "wanyi"));
                zh = new StringBuilder(zh.toString().replace("万", ""));
                zh = new StringBuilder(zh.toString().replace("wanyi", "万亿"));
            }
            zh.append(num).append(digit);
        }
        zh = new StringBuilder(deletezero(zh.toString()));
        return deleteOneTen(zh.toString());


    }

    /**
     * 如果最后一位数字是零，则删掉
     */
    private static String deletezero(String zh) {
        String lasts = (String) (zh.subSequence(zh.length() - 1, zh.length()));
        if ("零".equals(lasts)) {
            zh = zh.substring(0, zh.length() - 1);
        }
        return zh;
    }

    /**
     * 如果最后一位数字是零，则删掉
     */
    private static String deleteOneTen(String zh) {
        if (zh.startsWith("一十")) {
            return zh.substring(1);
        }
        return zh;
    }


}
