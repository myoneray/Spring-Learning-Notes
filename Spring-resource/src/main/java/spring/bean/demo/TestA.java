package spring.bean.demo;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author upsmart
 *
 */
public class TestA {
    private static ApplicationContext ctx;

    public static void main(String[] args) {
        System.out.println("******BeanFactory加载Bean的流程******");
        ctx = new ClassPathXmlApplicationContext("config/jpa.xml");
        Person p = ctx.getBean("person", Person.class);// 创建bean的引用对象
        ((ClassPathXmlApplicationContext) ctx).close();// 关闭应用上下文容器，不要忘记这句话
        p.info();
    }
}
