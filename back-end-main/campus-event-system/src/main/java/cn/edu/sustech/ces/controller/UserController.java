package cn.edu.sustech.ces.controller;

import cn.edu.sustech.ces.entity.Event;
import cn.edu.sustech.ces.entity.User;
import cn.edu.sustech.ces.repository.UserRepository;
import cn.edu.sustech.ces.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserRepository userRepository;
    private final UserService userService;

    @Autowired
    public UserController(UserRepository userRepository, UserService userService) {
        this.userRepository = userRepository;
        this.userService = userService;
    }

    @PostMapping ("query-id")
    public ResponseEntity<User> queryUserById(@RequestParam UUID userId) {
        User user = userService.getUserById(userId);
        return ResponseEntity.ok(user);
    }

}
