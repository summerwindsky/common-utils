package hyyd;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Title WritContextFormatter
 * @Description TODO
 * 
 * @author gaojun
 * @date 2015-2-5 下午4:39:48
 * @Company 北京华宇信息技术有限公司
 */

public class WritContextFormatter {

    private static final Logger logger = LoggerFactory.getLogger(WritContextFormatter.class);

    /**
     * 处理文书内容
     * 
     * @param context
     * @return
     */
    public static String dealWritContext(String context) {
        if (StringUtils.isEmpty(context)) {
            return null;
        }
        String text = context;
        // text = dealByRegex(text);
        text = dealWithYCFX(text);
        text = text.replaceAll("[\\s\u3000]*SHAPE[\\s\u3000]*[\\\\][\\*][\\s\u3000]*MERGEFORMAT[\\s\u3000]*", "");
        return text;
    }

    /*
     * private static String dealByRegex(String context) { String tempChar =
     * "&#xFFFD;"; String text = context; text =
     * text.replaceAll("(?<=[A-Za-z])\u0020(?=[A-Za-z])", tempChar); text =
     * text.replaceAll("(?<=[。：:;；])\u0020", "\n"); text = text .replaceAll(
     * "(?<=(^|[\u0020\n])[^\n，。；：,.;:、《》<>\u0020]{4,100}?)\u0020(?![\n，。；：,.;:《》<>])"
     * , "\n"); text = text.replaceAll(tempChar, "\u0020"); return text; }
     */

    /**
     * 由于经过多次docement<->String转换，文书内容丢失回车换行，在此做处理恢复回车换行
     * 
     * @param context
     * @return
     */
    private static String dealWithYCFX(String context) {
        String tempChar = "&#xFFFD;";
        String lk = "&#xFFFA;";
        String rk = "&#xFFFB;";
        String text = context;
        // 1 将需要保留的空格转换成一个肯定不会出现的字符，此处用\uFFFD
        // 1.1 处理括号中间的空格（.*? .*?）,由于括号嵌套，没处理一组先转其他字符
        boolean exist = true;
        StringBuffer buf = new StringBuffer();
        String regex = "[（(][^（）()\n]*?\u0020[^（）()\n]*?[)）]";
        Pattern p = Pattern.compile(regex);
        while (exist) {
            exist = false;
            buf.setLength(0);
            Matcher m = p.matcher(text);
            int start = 0;
            while (m.find(start)) {
                exist = true;
                buf.append(text.substring(start, m.start()));
                String temp = m.group(0);
                temp = temp.replaceAll("\u0020", tempChar);
                temp = temp.replaceAll("[(（]", lk);
                temp = temp.replaceAll("[）)]", rk);
                buf.append(temp);
                start = m.end();
            }
            buf.append(text.substring(start));
            text = buf.toString();
        }
        // 1.1.1 将括号转义回来
        text = text.replaceAll(lk, "（");
        text = text.replaceAll(rk, "）");
        // 1.2 处理英文字母中间的空格，常用于外国人名[A-Za-z] [A-Za-z]
        text = text.replaceAll("(?<=[A-Za-z])\u0020+(?=[A-Za-z])", tempChar);
        // 1.3 处理数字中间的空格，常用于金额
        text = text.replaceAll("(?<=[\\d.,])\u0020+(?=[\\d])", tempChar);
        text = text.replaceAll("(?<=[\\d])\u0020+(?=[\\d.,])", tempChar);

        // 2 替换空格为回车
        text = text.replaceAll("\u0020", "\n");
        // 3 恢复不该替换的空格
        text = text.replaceAll(tempChar, "\u0020");

        return text;
    }

    public static void writeOut(Document doc, String outFile, String name) {
        if (doc == null) {
            return;
        }
        File file = new File(outFile);
        try {
            if (!file.exists()) {
                file.mkdirs();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        try {
            OutputFormat outFmt = new OutputFormat();
            outFmt.setIndentSize(2);
            outFmt.setNewlines(true);

            XMLWriter writer = new XMLWriter(outFmt);
            if (!StringUtils.isBlank(outFile)) {
                outFmt.setEncoding("UTF-8");
                FileOutputStream fout = null;
                try {
                    fout = new FileOutputStream(outFile + File.separator + name);
                    writer.setOutputStream(fout);
                    writer.write(doc);
                } finally {
                    IOUtils.closeQuietly(fout);
                }
            } else {
                outFmt.setEncoding(Charset.defaultCharset().name());
                writer.setOutputStream(System.out);
                writer.write(doc);
            }
        } catch (IOException e) {
            logger.error("文件读写异常：" + e.getMessage());
        }
    }

    public static void main(String[] args) {
        String str = "人民币2  000  000元";
        str = "第14.1条和第14.3条";

        String res = dealWritContext(str);

        System.out.println(res);
    }

}
