package cn.edu.sustech.ces.controller;

import cn.edu.sustech.ces.entity.Location;
import cn.edu.sustech.ces.entity.User;
import cn.edu.sustech.ces.repository.LocationRepository;
import cn.edu.sustech.ces.repository.UserRepository;
import cn.edu.sustech.ces.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
public class UserController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;

    @GetMapping("/user-id")
    public User getUserById(UUID id) {
        return userService.getUserById(id);
    }

    @GetMapping("/create-user")
    public String createUser(
        @RequestParam String name,
        @RequestParam String email) {
        User user = new User(name, email);
        user = userRepository.save(user);
        return "User created with ID: " + user.getId();
    }

}
