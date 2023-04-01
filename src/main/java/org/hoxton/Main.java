package org.hoxton;

import org.winter.WinterApplicationContext;

public class Main {
    public static void main(String[] args) {

        WinterApplicationContext applicationContext = new WinterApplicationContext(AppConfig.class);

//        Object userService = applicationContext.getBean("userService");

//        UserService userService=applicationContext.getBean("userService",UserService.class);
//        userService.test();
        System.out.println("可以看到這三個的值是一模一樣的，代表這幾個物件都是同一個");
        System.out.println(applicationContext.getBean("userService"));
        System.out.println(applicationContext.getBean("userService"));
        System.out.println(applicationContext.getBean("userService"));
    }
}
