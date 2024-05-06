package cn.edu.sustech.ces.controller;

import cn.edu.sustech.ces.enums.PermissionGroup;
import cn.edu.sustech.ces.security.CESUserDetails;
import cn.edu.sustech.ces.security.LoginRequest;
import cn.edu.sustech.ces.entity.User;
import cn.edu.sustech.ces.security.RegisterRequest;
import cn.edu.sustech.ces.service.AuthService;
import cn.edu.sustech.ces.service.UserService;
import cn.edu.sustech.ces.utils.CESUtils;
import com.alibaba.fastjson.JSONObject;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;



import java.util.UUID;

@RestController
@RequestMapping("/api/user")
@AllArgsConstructor
public class UserController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final AuthService authService;

    //TODO: selectively expose user information

    @PostMapping ("/profile")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<User> fetchProfile() {
        CESUserDetails userDetails = (CESUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ResponseEntity.ok(userDetails.getUser());
    }

    //TODO: add CAPTCHA verification

    @PostMapping("/login")
    public ResponseEntity<JSONObject> authenticate(@RequestBody LoginRequest loginDto) {
        String token = authService.login(loginDto);

        CESUserDetails userDetails = (CESUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        JSONObject jwtAuthResponse = new JSONObject();
        jwtAuthResponse.put("access_token", token);
        jwtAuthResponse.put("token_type", "Bearer");
        jwtAuthResponse.put("user", userDetails.getUser());

        return ResponseEntity.ok(jwtAuthResponse);
    }

    //TODO: add register check and email verification

    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody RegisterRequest registerDto) {
        User newUser = userService.registerUser(registerDto.getNickname(), registerDto.getRealName(), "", registerDto.getEmail(), passwordEncoder.encode(registerDto.getPassword()), registerDto.getPhone(), PermissionGroup.USER);
        return ResponseEntity.ok(newUser);
    }

    @PostMapping("/update-profile")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<User> updateProfile(@RequestBody JSONObject request) {
        CESUserDetails userDetails = (CESUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userDetails.getUser();

        //TODO: only gender, birthday, description and avatar can be updated

        userService.updateUser(user);

        return ResponseEntity.ok(user);
    }

    @PostMapping("/list-orders")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<JSONObject> listOrders() {
        CESUserDetails userDetails = (CESUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userDetails.getUser();

        //TODO: list all user's orders

        return ResponseEntity.ok(null);
    }

    @PostMapping("/list-tickets")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<JSONObject> listTickets() {
        CESUserDetails userDetails = (CESUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userDetails.getUser();

        //TODO: list all user's tickets

        return ResponseEntity.ok(null);
    }

    @PostMapping("/change-permission")
    @PreAuthorize("hasAnyRole('INSTITUTE_ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<?> changePermission(@RequestParam UUID userId, @RequestParam Integer permissionGroup) {
        User currentUser = CESUtils.getAuthorizedUser();
        User user = userService.getUserById(userId);
        if (user == null) {
            return ResponseEntity.badRequest().body("User Not Found");
        }
        if (currentUser.getPermissionGroup().ordinal() <= user.getPermissionGroup().ordinal()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        user.setPermissionGroup(PermissionGroup.values()[permissionGroup]);
        userService.updateUser(user);
        return ResponseEntity.ok(user);
    }

}
