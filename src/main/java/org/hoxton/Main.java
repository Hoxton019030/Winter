package org.hoxton;

import org.hoxton.service.UserService;
import org.winter.WinterApplicationContext;

public class Main {
    public static void main(String[] args) {

        WinterApplicationContext applicationContext = new WinterApplicationContext(AppConfig.class);

//        Object userService = applicationContext.getBean("userService");

//        UserService userService=applicationContext.getBean("userService",UserService.class);
//        userService.test();
        UserService userService = (UserService) applicationContext.getBean("userService");
        userService.test();
    }
}
