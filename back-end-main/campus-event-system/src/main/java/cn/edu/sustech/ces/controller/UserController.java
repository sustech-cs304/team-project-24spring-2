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

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;

    @GetMapping("/user-id")
    public User getUserById(UUID id) {
        return userService.getUserById(id);
    }

    @GetMapping("/search-users")
    public List<User> searchUsers(@RequestParam String field, @RequestParam String value) {
        return userService.searchUsersByField(field, value);
    }

    @PostMapping("/create-user")
    public String createUser(
        @RequestParam String nickname,
        @RequestParam String email) {
        User user = new User(nickname, email);
        user = userRepository.save(user);
        return "User created with ID: " + user.getId();
    }

    /*
    * 该方法在api里传入user json
    * */
    @PutMapping("/update-user")
    public ResponseEntity<?> updateUser(@RequestBody User newUser) {
        if (newUser.getId() == null) {
            return ResponseEntity.badRequest().body("User ID cannot be null.");
        }

        return userRepository.findById(newUser.getId())
                .map(user -> {
                    // 更新字段，仅当新值不为 null 时
                    if (newUser.getNickname() != null) user.setNickname(newUser.getNickname());
                    if (newUser.getEmail() != null) user.setEmail(newUser.getEmail());
                    if (newUser.getNickname() != null) user.setNickname(newUser.getNickname());
                    if (newUser.getRealname() != null) user.setRealname(newUser.getRealname());
                    if (newUser.getDescription() != null) user.setDescription(newUser.getDescription());
                    if (newUser.getPassword() != null) user.setPassword(newUser.getPassword());
                    if (newUser.getPhone() != null) user.setPhone(newUser.getPhone());

                    // 保存更新后的用户
                    userRepository.save(user);
                    return ResponseEntity.ok("User updated with ID: " + user.getId());
                })
                .orElseGet(() -> ResponseEntity.notFound().build()); // 如果找不到用户，返回404
    }



    @DeleteMapping("/delete-user")
    public User deleteUser(@RequestParam UUID id) {
        return userService.deleteUserById(id);
    }

    @GetMapping("/user-events")
    public List<Event> getUserEvents(@RequestParam String type,@RequestParam UUID userId) {
        return userService.findEventsByUser(type, userId);
    }

}
