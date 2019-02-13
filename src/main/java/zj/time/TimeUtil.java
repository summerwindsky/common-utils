package zj.time;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.List;

/**
 * Title:
 * Description:
 * Company: 北京华宇元典信息服务有限公司
 *
 * @author zhangjing
 * @version 1.0
 * @date 2018-10-11 16:32
 */
public class TimeUtil {

    private static final Logger logger = LoggerFactory.getLogger(TimeUtil.class);

    //    private static String path = ResourcePath.getClassPath() + TimedJobConfig.TIMEDTASK_TIMESTAMP_PATH;
    private static String path = "";

    private static File file = new File(path);

    /**
     * 保存时间戳到文件
     */
    public static void saveCurrTimestamp() {
        try {
            String time = System.currentTimeMillis() + "";
            FileUtils.writeStringToFile(file, time, "utf-8");
            logger.info("保存时间戳：{}", time);
        } catch (Exception e) {
            logger.info("保存时间戳出错！");
        }
    }

    /**
     * 获取文件中的时间戳
     * @return
     */
    public static String getTimestamp() {
        String time = null;
        if (file.exists()) {
            try {
                List<String> lines = FileUtils.readLines(file, "utf-8");
                if (CollectionUtils.isNotEmpty(lines)) {
                    time = lines.get(0);
                    logger.info("从文件获取时间戳：{}", time);
                    return time;
                }
            } catch (Exception e) {
                logger.error("读取时间戳出错：{}", e);
            }
        }
        logger.error("读取时间戳出错,时间戳文件不存在");
        return null;
    }
}
