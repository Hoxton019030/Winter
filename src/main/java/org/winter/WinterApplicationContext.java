package org.winter;

import javax.crypto.spec.IvParameterSpec;
import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Winter的容器類
 */
public class WinterApplicationContext {

    final String SINGLETON ="singleton";
    private Class configClass;

    /**
     * 一個單例池，存放Spring Bean
     */
    private ConcurrentHashMap<String, Object> singletonPool = new ConcurrentHashMap<>();
    /**
     * 存放所有Spring Bean的定義
     */
    private ConcurrentHashMap<String, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<>();

    /**
     * @param configClass Winter的配置文件
     */
    public WinterApplicationContext(Class configClass) {
        this.configClass = configClass;
        //解析配置類
        // ComponentScan註解解析 -> 掃描路徑 -> 掃描 ---> BeanDefinition ---> BeanDefinitionMap
        scan(configClass);

        for (String beanName : beanDefinitionMap.keySet()) {
            BeanDefinition beanDefinition = beanDefinitionMap.get(beanName);
            //如果bean scope是單例，則放進單例池中
            if(beanDefinition.getScope().equals(SINGLETON)){
                Object bean = creatBean(beanDefinition);
                System.out.println("beanDefinition.getScope() = " + beanDefinition.getScope());
                System.out.println("beanDefinition.getClazz() = " + beanDefinition.getClazz());
                System.out.println("beanName = " + beanName);
                singletonPool.put(beanName, bean);
            }

        }


    }

    public Object creatBean(BeanDefinition beanDefinition){
        Class clazz = beanDefinition.getClazz();
        try {
            Object instance = clazz.getDeclaredConstructor().newInstance();
            return instance;
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    private void scan(Class configClass) {
        ComponentScan componentScanAnnotation = (ComponentScan) configClass.getAnnotation(ComponentScan.class);
        String path = componentScanAnnotation.value(); //掃描路徑

        //掃描 org.hoxton.service
        // 類加載器 (Class Loader)
        // Java中有三種類加載器，以及對應的加載路徑
        // BootStrap ---> jre/lib
        // Ext ---> jre/ext/lib
        // App ---> classpath --->  "C:\Program Files\Java\jdk-11\bin\java.exe" "-javaagent:C:\Program Files\JetBrains\IntelliJ IDEA 2022.2.2\lib\idea_rt.jar=5445:C:\Program Files\JetBrains\IntelliJ IDEA 2022.2.2\bin" -Dfile.encoding=UTF-8 -classpath C:\Users\za546\Desktop\Winter\target\classes org.hoxton.Test 編譯器顯示的資訊

        ClassLoader classLoader = WinterApplicationContext.class.getClassLoader(); //app加載器
        URL resource = classLoader.getResource("org/hoxton/service"); //使用類加載，掃苗檔案下的.class檔
        File file = new File(resource.getFile());
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (File f : files) {
                String fileName = f.getAbsolutePath();
                if (fileName.endsWith(".class")) {
                    String className = fileName.substring(fileName.indexOf("org"), fileName.indexOf(".class"));
                    className = className.replace("\\", ".");
                    Class<?> clazz = null;
                    try {
                        clazz = classLoader.loadClass(className);
                    } catch (ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                    if (clazz.isAnnotationPresent(Component.class)) {
                        //表示這個類是個Bean
                        // ...? Class -- > bean ?
                        // 解析類，判斷當前Bean是單例Bean還是Prototype的Bean，生成BeanDefinition

                        Component componentAnnotation = clazz.getDeclaredAnnotation(Component.class);
                        String beanName = componentAnnotation.value();

                        //BeanDefinition- Bean定義
                        BeanDefinition beanDefinition = new BeanDefinition();
                        beanDefinition.setClazz(clazz);
                        if (clazz.isAnnotationPresent(Scope.class)) {
                            Scope scopeAnnotation = clazz.getDeclaredAnnotation(Scope.class);
                            beanDefinition.setScope(scopeAnnotation.value());
                        } else {
                            beanDefinition.setScope(SINGLETON);
                        }

                        beanDefinitionMap.put(beanName, beanDefinition);

                    }

                }
//

            }

        }
    }

    public Object getBean(String beanName) {
        // 依照beanName去判斷是單例Bean還是Prototype Bean
        if (beanDefinitionMap.containsKey(beanName)) {
            BeanDefinition beanDefinition = beanDefinitionMap.get(beanName);
            if (beanDefinition.getScope().equals(SINGLETON)) {
                Object bean = singletonPool.get(beanName);
                return bean;
            } else {
            //創建bean對象嗎?
                Object bean = creatBean(beanDefinition);
                return bean;
            }
        } else {
            throw new NullPointerException("沒有這個Bean");
        }
    }
}