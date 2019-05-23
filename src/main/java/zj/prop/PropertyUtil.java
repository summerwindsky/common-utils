package zj.prop;

import java.util.HashMap;
import java.util.Map;

/**
 * @author dingpeng
 * @version 1.0
 * @date 2018/8/1 17:40
 */
public class PropertyUtil {

    private static Map<String, String> propertyMap;

    private static String activeFileName;

    private static volatile PropertyUtil instance;

    private PropertyUtil(Map<String, String> map, String fileName) {
        propertyMap = map;
        activeFileName = fileName;
    }

    public static PropertyUtil getInstance(Map<String, String> properties, String fileName) {
        if (instance == null) {
            synchronized (PropertyUtil.class) {
                if (instance == null) {
                    instance = new PropertyUtil(properties, fileName);
                }
            }
        }
        return instance;
    }

    public static Map<String, String> getProperties(String prefix) {
        Map<String, String> map = new HashMap<>();
        if (propertyMap != null) {
            propertyMap.forEach((key, value) -> {
                if (key.startsWith(prefix)) {
                    map.put(key, value);
                }
            });
        }
        return map;
    }

    public static String getProperty(String name) {
        if (propertyMap != null) {
            return propertyMap.get(name);
        }
        return null;
    }

    public static String getActiveFileName() {
        if (activeFileName != null) {
            return activeFileName;
        }
        return null;
    }

    public static boolean validate(String name) {
        if (activeFileName != null) {
            return activeFileName.equals("application-" + name + ".properties");
        }
        return false;
    }

    public synchronized static boolean update(String key, String value) {
        if (propertyMap.get(key) == null) {
            return false;
        }
        propertyMap.put(key, value);
        return true;
    }
}
