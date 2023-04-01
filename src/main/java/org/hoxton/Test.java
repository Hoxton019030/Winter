package org.hoxton;

import org.hoxton.service.UserService;
import org.winter.WinterApplicationContext;

public class Test {
    public static void main(String[] args) {

        WinterApplicationContext applicationContext = new WinterApplicationContext(AppConfig.class);

//        Object userService = applicationContext.getBean("userService");

//        UserService userService=applicationContext.getBean("userService",UserService.class);
//        userService.test();
        System.out.println(applicationContext.getBean("userService"));
        System.out.println(applicationContext.getBean("userService"));
        System.out.println(applicationContext.getBean("userService"));
    }
}
