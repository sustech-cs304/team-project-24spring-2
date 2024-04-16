package cn.edu.sustech.ces.controller;

import cn.edu.sustech.ces.enums.PermissionGroup;
import cn.edu.sustech.ces.security.CESUserDetails;
import cn.edu.sustech.ces.security.LoginRequest;
import cn.edu.sustech.ces.entity.User;
import cn.edu.sustech.ces.security.RegisterRequest;
import cn.edu.sustech.ces.service.AuthService;
import cn.edu.sustech.ces.service.UserService;
import com.alibaba.fastjson.JSONObject;
import lombok.AllArgsConstructor;
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

    @PostMapping ("/profile")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<User> fetchProfile() {
        CESUserDetails userDetails = (CESUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ResponseEntity.ok(userDetails.getUser());
    }

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

    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody RegisterRequest registerDto) {
        User newUser = userService.registerUser(registerDto.getNickname(), registerDto.getRealName(), "", registerDto.getEmail(), passwordEncoder.encode(registerDto.getPassword()), registerDto.getPhone(), PermissionGroup.USER);
        return ResponseEntity.ok(newUser);
    }

}
