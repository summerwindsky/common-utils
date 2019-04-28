package zj.file;

import org.apache.tika.Tika;
import org.apache.tika.exception.TikaException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * Title: DealWritRelationHandler
 * Description:
 * Company: 北京华宇元典信息服务有限公司
 *
 * @author tianxiupeng
 * @version 1.0
 * @date 2019/3/27
 */
public class TikaUtils {

	private static Logger logger = LoggerFactory.getLogger(TikaUtils.class);
	private static Tika tika;

	public static synchronized Tika getInstance() {
		if (null == tika) {
			tika = new Tika();
			tika.setMaxStringLength(-1);
		}
		return tika;
	}

	public static String parseToString(File file) {
		String value = null;
		try {
			value = getInstance().parseToString(file);
		} catch (IOException e) {
			logger.error("io解析失败！" + file.getAbsolutePath(), e);
		} catch (TikaException e) {
			logger.error("tika 解析失败！" + file.getAbsolutePath(), e);
		}
		return value;
	}
	public static String parseToString(InputStream file) {
		String value = null;
		try {
			value = getInstance().parseToString(file);
		} catch (Exception e) {
e.printStackTrace();
		}
		return value;
	}
}
