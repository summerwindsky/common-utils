package zj.prop;

import lombok.extern.log4j.Log4j2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationPreparedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 *  配置监听器
 * @author dingpeng
 * @version 1.0
 * @date 2018/8/1 18:18
 */
@Log4j2
public class PropertyListener implements ApplicationListener<ApplicationPreparedEvent> {

    private static final Logger LOGGER = LoggerFactory.getLogger(PropertyListener.class);

    @Override
    public void onApplicationEvent(ApplicationPreparedEvent applicationPreparedEvent) {
        String[] activeFiles = applicationPreparedEvent.getApplicationContext().getEnvironment().getActiveProfiles();
        String fileName = "application.properties";
        Map<String, String> map = new HashMap<>();
        getProperties(fileName, map);
        log.info("读取默认配置文件【{}】完成", fileName);
        for (String s : activeFiles) {
            fileName = "application-" + s + ".properties";
            getProperties(fileName, map);
            log.info("读取自定义配置文件【{}】完成", fileName);
        }
        PropertyUtil.getInstance(map, fileName);
    }

    private void getProperties(String fileName, Map<String, String> map) {
        try {
            Resource resource = new ClassPathResource(fileName);
            InputStream is = resource.getInputStream();
            Properties ps = new Properties();
            ps.load(is);

            ps.forEach((key, value) -> map.put(String.valueOf(key), value == null ? null : String.valueOf(value)));
        } catch (IOException e) {
            LOGGER.error("读取配置文件异常：", e);
        }
    }

    public static Map<String, String> getProperties(String fileName) {
        try {
            Map<String, String> map = new HashMap<>();
            Resource resource = new ClassPathResource(fileName);
            InputStream is = resource.getInputStream();
            Properties ps = new Properties();
            ps.load(is);

            ps.forEach((key, value) -> map.put(String.valueOf(key), value == null ? null : String.valueOf(value)));
            return map;
        } catch (IOException e) {
            LOGGER.error("读取配置文件异常：", e);
        }
        return null;
    }

}
