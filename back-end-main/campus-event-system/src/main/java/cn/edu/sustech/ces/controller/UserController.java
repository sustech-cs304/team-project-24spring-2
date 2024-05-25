package cn.edu.sustech.ces.controller;

import cn.edu.sustech.ces.entity.Order;
import cn.edu.sustech.ces.entity.Ticket;
import cn.edu.sustech.ces.enums.PermissionGroup;
import cn.edu.sustech.ces.enums.UserGender;
import cn.edu.sustech.ces.security.CESUserDetails;
import cn.edu.sustech.ces.security.JwtTokenProvider;
import cn.edu.sustech.ces.security.LoginRequest;
import cn.edu.sustech.ces.entity.User;
import cn.edu.sustech.ces.security.RegisterRequest;
import cn.edu.sustech.ces.service.*;
import cn.edu.sustech.ces.service.minio.MinioService;
import cn.edu.sustech.ces.utils.CESUtils;
import com.alibaba.fastjson.JSONObject;
import lombok.AllArgsConstructor;
import okhttp3.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;


import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/user")
@AllArgsConstructor
public class UserController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final AuthService authService;
    private final OrderService orderService;
    private final MinioService minioService;
    private final TicketService ticketService;
    private final VerifyCodeService codeService;
    private final MailService mailService;

    private final JwtTokenProvider jwtTokenProvider;

    //TODO: selectively expose user information

    @PostMapping ("/profile")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<User> fetchProfile() {
        CESUserDetails userDetails = (CESUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ResponseEntity.ok(userDetails.getUser());
    }

    @PostMapping("/verify")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> verifyLoginState() {
        return ResponseEntity.ok("Verified");
    }

    @PostMapping("/get-user")
    public ResponseEntity<?> getUser(@RequestParam UUID userId) {
        User user = userService.getUserById(userId);
        if (user == null) {
            return ResponseEntity.badRequest().body("User Not Found");
        }
        String nickName = user.getNickname();
        String avatarUrl = user.getAvatarUrl();
        int permissionGroup = user.getPermissionGroup().ordinal();
        String email = user.getEmail();
        long birthday = user.getBirthday();
        UserGender gender = user.getGender();
        JSONObject response = new JSONObject();
        response.put("nickname", nickName);
        response.put("avatar_url", avatarUrl);
        response.put("permission_group", permissionGroup);
        response.put("email", email);
        response.put("birthday", birthday);
        response.put("gender", gender);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/get-full-user")
    @PreAuthorize("hasAnyRole('INSTITUTE_ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<?> getFullUser(@RequestParam UUID userId) {
        User user = userService.getUserById(userId);
        if (user == null) {
            return ResponseEntity.badRequest().body("User Not Found");
        }
        return ResponseEntity.ok(user);
    }

    @PostMapping("/list-user-size")
    @PreAuthorize("hasAnyRole('INSTITUTE_ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<?> listUserSize(@RequestParam(required = false) String nickname, @RequestParam(required = false) String email) {
        return ResponseEntity.ok(userService.listUserSize(nickname, email));
    }

    @PostMapping("/list-user")
    @PreAuthorize("hasAnyRole('INSTITUTE_ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<?> listUser(@RequestParam(required = false) String nickname, @RequestParam(required = false) String email) {
        return ResponseEntity.ok(userService.listUser(nickname, email));
    }

    @PostMapping("/get-user-name")
    public ResponseEntity<?> getUserByName(@RequestParam String nickname) {
        User user = userService.getUserByNickname(nickname);
        if (user == null) {
            return ResponseEntity.badRequest().body("User Not Found");
        }
        String uuid = user.getId().toString();
        String avatarUrl = user.getAvatarUrl();
        int permissionGroup = user.getPermissionGroup().ordinal();
        JSONObject response = new JSONObject();
        response.put("uuid", uuid);
        response.put("avatar_url", avatarUrl);
        response.put("permission_group", permissionGroup);
        return ResponseEntity.ok(response);
    }

    //TODO: add CAPTCHA verification

    @PostMapping("/login")
    public ResponseEntity<JSONObject> authenticate(@RequestBody LoginRequest loginDto) {
        String token = authService.login(loginDto);

        CESUserDetails userDetails = (CESUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        JSONObject jwtAuthResponse = new JSONObject();
        jwtAuthResponse.put("access_token", token);
        jwtAuthResponse.put("token_type", "Bearer");
        jwtAuthResponse.put("expire_time", System.currentTimeMillis() + jwtTokenProvider.getExpirationTime() - 1000);
        jwtAuthResponse.put("user", userDetails.getUser());

        return ResponseEntity.ok(jwtAuthResponse);
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest registerDto) {
        String code = registerDto.getVerifyCode();
        String storageCode = codeService.getCode(registerDto.getEmail());
        if (storageCode == null || !storageCode.equals(code)) {
            return ResponseEntity.badRequest().body("Invalid Code");
        }
        User search = userService.getUserByNickname(registerDto.getNickname());
        if (search != null) {
            return ResponseEntity.badRequest().body("Nickname already exists");
        }
        User newUser = userService.registerUser(registerDto.getNickname(),
                registerDto.getRealName(),
                "",
                registerDto.getEmail(),
                passwordEncoder.encode(registerDto.getPassword()),
                registerDto.getPhone(),
                PermissionGroup.USER);
        codeService.removeCode(registerDto.getEmail());
        return ResponseEntity.ok(newUser);
    }

    @PostMapping("/fetch-register-code")
    public ResponseEntity<?> fetchRegisterCode(@RequestParam String email) {
        String code = codeService.getCode(email);
        if (code != null) {
            long time = codeService.getCodeTime(email);
            if (System.currentTimeMillis() - time < 60000) {
                return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body("Please wait for 1 minute before requesting another code");
            }
        }
        code = codeService.generateCode();
        codeService.storeCode(email, code);
        mailService.sendSimpleMessage(email, "[CES] Verification Code", "Your verification code is: " + code);
        return ResponseEntity.ok("Code Sent");
    }

    @PostMapping("/update-profile")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<User> updateProfile(@RequestBody JSONObject request) {
        CESUserDetails userDetails = (CESUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userDetails.getUser();

        if (request.containsKey("gender")) {
            user.setGender(UserGender.valueOf(request.getString("gender").toUpperCase()));
        }
        if (request.containsKey("birthday")) {
            user.setBirthday(request.getLong("birthday"));
        }
        if (request.containsKey("description")) {
            user.setDescription(request.getString("description"));
        }
        if (request.containsKey("password")) {
            user.setPassword(passwordEncoder.encode(request.getString("password")));
        }
        if (request.containsKey("phone")) {
            user.setPhone(request.getString("phone"));
        }

        userService.updateUser(user);

        return ResponseEntity.ok(user);
    }

    @PostMapping("/list-orders")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> listOrders() {
        CESUserDetails userDetails = (CESUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userDetails.getUser();

        List<Order> orders = orderService.getUserOrders(user.getId());

        return ResponseEntity.ok(orders);
    }

    @PostMapping("/list-tickets")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> listTickets() {
        CESUserDetails userDetails = (CESUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userDetails.getUser();

        List<Ticket> tickets = new ArrayList<>();

        user.getUserTickets().forEach(ticketId -> {
            Ticket t = ticketService.getTicketById(ticketId);
            if (t != null) {
                tickets.add(t);
            }
        });

        return ResponseEntity.ok(tickets);
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

    @PostMapping("/get-upload-images")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getUploadImages() {
        User user = CESUtils.getAuthorizedUser();
        List<String> urls = minioService.getItems(minioService.getImageBucket(), "user/" + user.getId().toString())
                .stream()
                .map(item -> "/" + minioService.getImageBucket() + "/" + item.objectName())
                .toList();
        return ResponseEntity.ok(urls);
    }

}
