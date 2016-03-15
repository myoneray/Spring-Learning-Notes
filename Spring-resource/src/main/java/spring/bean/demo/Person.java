package spring.bean.demo;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.ResourceLoader;

/**
 * ClassName: Person <br>
 *
 * @author WangMing
 * @since 2016年3月9日 下午2:36:54
 */
public class Person implements InitializingBean, DisposableBean, BeanNameAware, BeanClassLoaderAware, BeanFactoryAware, ResourceLoaderAware, ApplicationEventPublisherAware, MessageSourceAware,
        ApplicationContextAware {
    private String name;
    private int age;
    private ResourceLoader myResourceLoader;

    public Person() {
        System.out.println("01>调用Bean的默认构造方法");
    }

    public void setBeanName(String name) {
        System.out.println("02>检查Bean配置文件中是否注入了Bean的属性值:设置bean的名称" + name);
    }

    public void setBeanClassLoader(ClassLoader classLoader) {
        System.out.println("03>我知道是:" + classLoader + "装载的我!");
    }

    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        System.out.println("04>我是属于工厂:" + beanFactory + "的!");
    }

    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.myResourceLoader = resourceLoader;
        System.out.println("05>我的数据源是:" + myResourceLoader + "!");
    }

    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        System.out.println("06>我是由事件发布者:" + applicationEventPublisher + "发布的.");
    }

    public void setMessageSource(MessageSource messageSource) {
        System.out.println("07>我的MessageSource对象:" + messageSource + "!");
    }

    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        System.out.println("08>我的applicationContext:" + applicationContext);
        System.out.println("09>实现ServletContextAware可以获取ServletContext");
        System.out.println("10>实现BeanPostProcessor#postProcessBeforeInitialization()可扩展容器!");
    }

    public void afterPropertiesSet() throws Exception {
        /**
         * @see InitializingBean
         */
        System.out.println("11>检查Bean是否实现了InitializingBean接口");
        /**
         * @see RootBeanDefinition
         */
        System.out.println("12>实现RootBeanDefinition可以干啥!我不是很清楚!");
        /**
         * @see BeanPostProcessor
         */
        System.out.println("13>实现BeanPostProcessor#postProcessAfterInitialization()可扩展容器!");
    }

    public void init() {
        System.out.println("14>检查Bean配置文件中是否指定了init-method此属性");
        /**
         * @see RootBeanDefinition
         */
        System.out.println("15>实现RootBeanDefinition可以干啥!我不是很清楚!");

    }

    // 当且仅当 配置:scope="singleton"时,Spring会自动销毁该Bean!即为单例模式.
    /**
     * @see org.springframework.beans.factory.DisposableBean#destroy()
     */
    public void destroy() throws Exception {
        System.out.println("16>调用DisposableBean#destroy服务停止");
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void info() {
        System.out.println("name:" + getName() + " age:" + getAge());
    }

}
