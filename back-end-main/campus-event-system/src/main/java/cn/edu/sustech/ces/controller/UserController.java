package cn.edu.sustech.ces.controller;

import cn.edu.sustech.ces.entity.User;
import cn.edu.sustech.ces.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/user-id")
    public User getUserById(int id) {
        return userService.getUserById(id);
    }

}
