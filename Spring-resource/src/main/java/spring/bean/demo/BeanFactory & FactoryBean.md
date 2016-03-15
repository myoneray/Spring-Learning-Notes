BeanFactory和FactoryBean

1、 BeanFactory
    BeanFactory定义了 IOC 容器的最基本形式，并提供了 IOC 容器应遵守的的最基本的接口，也就是 Spring IOC 所遵守的最底层和最基本的编程规范。在  Spring 代码中， BeanFactory 只是个接口，并不是 IOC 容器的具体实现，但是 Spring 容器给出了很多种实现，如 DefaultListableBeanFactory 、 XmlBeanFactory 、 ApplicationContext 等，都是附加了某种功能的实现。

Java代码  收藏代码
package org.springframework.beans.factory;  
import org.springframework.beans.BeansException;  
public interface BeanFactory {  
    String FACTORY_BEAN_PREFIX = "&";  
    Object getBean(String name) throws BeansException;  
    <T> T getBean(String name, Class<T> requiredType) throws BeansException;  
    <T> T getBean(Class<T> requiredType) throws BeansException;  
    Object getBean(String name, Object... args) throws BeansException;  
    boolean containsBean(String name);  
    boolean isSingleton(String name) throws NoSuchBeanDefinitionException;  
    boolean isPrototype(String name) throws NoSuchBeanDefinitionException;  
    boolean isTypeMatch(String name, Class<?> targetType) throws NoSuchBeanDefinitionException;  
    Class<?> getType(String name) throws NoSuchBeanDefinitionException;  
    String[] getAliases(String name);  
}   
 
 
2、 FactoryBean
    一般情况下，Spring 通过反射机制利用 <bean> 的 class 属性指定实现类实例化 Bean ，在某些情况下，实例化 Bean 过程比较复杂，如果按照传统的方式，则需要在 <bean> 中提供大量的配置信息。配置方式的灵活性是受限的，这时采用编码的方式可能会得到一个简单的方案。 Spring 为此提供了一个 org.springframework.bean.factory.FactoryBean 的工厂类接口，用户可以通过实现该接口定制实例化 Bean 的逻辑。
FactoryBean接口对于 Spring 框架来说占用重要的地位， Spring 自身就提供了 70 多个 FactoryBean 的实现。它们隐藏了实例化一些复杂 Bean 的细节，给上层应用带来了便利。从 Spring 3.0 开始， FactoryBean 开始支持泛型，即接口声明改为 FactoryBean<T> 的形式：

Java代码  收藏代码
package org.springframework.beans.factory;  
public interface FactoryBean<T> {  
    T getObject() throws Exception;  
    Class<?> getObjectType();  
    boolean isSingleton();  
}   
 
在该接口中还定义了以下3 个方法：
T getObject()：返回由 FactoryBean 创建的 Bean 实例，如果 isSingleton() 返回 true ，则该实例会放到 Spring 容器中单实例缓存池中；
boolean isSingleton()：返回由 FactoryBean 创建的 Bean 实例的作用域是 singleton 还是 prototype ；
Class<T> getObjectType()：返回 FactoryBean 创建的 Bean 类型。
当配置文件中<bean> 的 class 属性配置的实现类是 FactoryBean 时，通过 getBean() 方法返回的不是 FactoryBean 本身，而是 FactoryBean#getObject() 方法所返回的对象，相当于 FactoryBean#getObject() 代理了 getBean() 方法。
例：如果使用传统方式配置下面Car 的 <bean> 时， Car 的每个属性分别对应一个 <property> 元素标签。

Java代码  收藏代码
package  com.baobaotao.factorybean;  
    public   class  Car  {  
        private   int maxSpeed ;  
        private  String brand ;  
        private   double price ;  
        public   int  getMaxSpeed ()   {  
            return   this . maxSpeed ;  
        }  
        public   void  setMaxSpeed ( int  maxSpeed )   {  
            this . maxSpeed  = maxSpeed;  
        }  
        public  String getBrand ()   {  
            return   this . brand ;  
        }  
        public   void  setBrand ( String brand )   {  
            this . brand  = brand;  
        }  
        public   double  getPrice ()   {  
            return   this . price ;  
        }  
        public   void  setPrice ( double  price )   {  
            this . price  = price;  
       }  
}   
 
如果用FactoryBean 的方式实现就灵活点，下例通过逗号分割符的方式一次性的为 Car 的所有属性指定配置值：

Java代码  收藏代码
package  com.baobaotao.factorybean;  
import  org.springframework.beans.factory.FactoryBean;  
public   class  CarFactoryBean  implements  FactoryBean<Car>  {  
    private  String carInfo ;  
    public  Car getObject ()   throws  Exception  {  
        Car car =  new  Car () ;  
        String []  infos =  carInfo .split ( "," ) ;  
        car.setBrand ( infos [ 0 ]) ;  
        car.setMaxSpeed ( Integer. valueOf ( infos [ 1 ])) ;  
        car.setPrice ( Double. valueOf ( infos [ 2 ])) ;  
        return  car;  
    }  
    public  Class<Car> getObjectType ()   {  
        return  Car. class ;  
    }  
    public   boolean  isSingleton ()   {  
        return   false ;  
    }  
    public  String getCarInfo ()   {  
        return   this . carInfo ;  
    }  
  
 接受逗号分割符设置属性信息  
    public   void  setCarInfo ( String carInfo )   {  
        this . carInfo  = carInfo;  
    }  
}   
 
有了这个CarFactoryBean 后，就可以在配置文件中使用下面这种自定义的配置方式配置 Car Bean 了：
<bean id="car" class="com.baobaotao.factorybean.CarFactoryBean" P:carInfo="法拉利 ,400,2000000"/>
当调用getBean("car") 时， Spring 通过反射机制发现 CarFactoryBean 实现了 FactoryBean 的接口，这时 Spring 容器就调用接口方法 CarFactoryBean#getObject() 方法返回。如果希望获取 CarFactoryBean 的实例，则需要在使用 getBean(beanName) 方法时在 beanName 前显示的加上 "&" 前缀：如 getBean("&car");
 
3、 区别
    BeanFactory是个 Factory ，也就是 IOC 容器或对象工厂， FactoryBean 是个 Bean 。在 Spring 中，所有的 Bean 都是由 BeanFactory( 也就是 IOC 容器 ) 来进行管理的。但对 FactoryBean 而言，这个 Bean 不是简单的 Bean ，而是一个能生产或者修饰对象生成的工厂 Bean, 它的实现与设计模式中的工厂模式和修饰器模式类似。
 