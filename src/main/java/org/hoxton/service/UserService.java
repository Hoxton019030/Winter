package org.hoxton.service;

import org.winter.annotation.Autowired;
import org.winter.annotation.Component;

/**
 * @author Hoxton
 * @version 0.1.0
 * @since 0.1.0
 **/
@Component("userService")
public class UserService {

    @Autowired
    private OrderService orderService;

    public void test(){
        System.out.println("orderService = " + orderService);
    }
}
