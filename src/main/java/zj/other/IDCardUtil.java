package zj.other;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Title:
 * Description:
 * Company: 北京华宇元典信息服务有限公司
 *
 * @author zhangjing
 * @version 1.0
 * @date 2018-07-16 17:33
 */
public class IDCardUtil {

    private static final Logger logger = LoggerFactory.getLogger(IDCardUtil.class);

    /**
     * 获取性别
     *
     * @param id
     * @return
     */
    public static String getGenderByIdCard(String id) {
        String idTrimed = id.trim();
        if (StringUtils.isEmpty(idTrimed)) {
            return null;
        }
        String gender = null;

        int length = idTrimed.length();

        try {
            Integer num = null;
            if (length == 15) {
                // 15位长度，取最后一位
                num = Integer.valueOf(id.substring(length - 2, length - 1));
            } else if (length == 18) {
                // 18位，取第17位
                num = Integer.valueOf(id.substring(16, 17));
            } else {
                return null;
            }

            if (num % 2 == 0) {
                gender = "女";
            } else {
                gender = "男";
            }
        } catch (NumberFormatException e) {
            logger.error("id exception:{}, id:{}", e, idTrimed);
            e.printStackTrace();
        }

        return gender;
    }
}
