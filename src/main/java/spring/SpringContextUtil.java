package spring;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class SpringContextUtil implements ApplicationContextAware {

    private static ApplicationContext applicationContext = null;

    /***
     * 当继承了ApplicationContextAware类之后，那么程序在调用 getBean(String)的时候会自动调用该方法，不用自己操作
     */
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringContextUtil.applicationContext = applicationContext;
    }

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    /***
     * 根据一个bean的id获取配置文件中相应的bean
     * 
     * @param name
     * @return
     * @throws BeansException
     */
    public static Object getBean(String name) throws BeansException {
        return applicationContext.getBean(name);
    }

    /***
     * 类似于getBean(String name)只是在参数中提供了需要返回到的类型。
     * 
     * @param name
     * @param requiredType
     * @return
     * @throws BeansException
     */
    public static Object getBean(String name, Class<?> requiredType) throws BeansException {
        return applicationContext.getBean(name, requiredType);
    }

    /**
     * 如果BeanFactory包含一个与所给名称匹配的bean定义，则返回true
     * 
     * @param name
     * @return boolean
     */
    public static boolean containsBean(String name) {
        return applicationContext.containsBean(name);
    }

}
