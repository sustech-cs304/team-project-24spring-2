package cn.edu.sustech.ces.controller;

import cn.edu.sustech.ces.entity.Event;
import cn.edu.sustech.ces.entity.Location;
import cn.edu.sustech.ces.entity.User;
import cn.edu.sustech.ces.entity.UserCreateEvent;
import cn.edu.sustech.ces.repository.LocationRepository;
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
        @RequestParam String name,
        @RequestParam String email) {
        User user = new User(name, email);
        user = userRepository.save(user);
        return "User created with ID: " + user.getId();
    }

    /*
    * 该方法在api里传入user json
    * */
    @PutMapping("/update-user")
    public ResponseEntity<?> updateUser(@RequestBody User user) {
        if (!userRepository.existsById(user.getId())) {
            return ResponseEntity.notFound().build();
        }
        userRepository.save(user);
        return ResponseEntity.ok("User updated with ID: " + user.getId());
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
