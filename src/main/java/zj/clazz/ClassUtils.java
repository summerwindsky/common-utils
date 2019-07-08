package zj.clazz;

import org.reflections.Reflections;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Log4j2
public class ClassUtils {

    /**
     * 根据接口，获取本jar包中的所有实现类
     *
     * @param c
     * @return
     */
    public static List<Class> getAllImplByInterface(Class c) {
        List returnClassList = new ArrayList<Class>();
        //判断是不是接口,不是接口不作处理
        if (c.isInterface()) {
            String packageName = c.getPackage().getName();    //获得当前包名
            try {
                Reflections reflections = new Reflections(packageName);
                Set<Class> modules = reflections.getSubTypesOf(c);
                returnClassList.addAll(modules);
            } catch (Exception e) {
                log.error("获取接口实现类异常，接口：{} ", c, e);
            }
        }
        return returnClassList;
    }

}
