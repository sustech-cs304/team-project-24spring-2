package cn.edu.sustech.ces.initialize;

import cn.edu.sustech.ces.entity.User;
import cn.edu.sustech.ces.enums.PermissionGroup;
import cn.edu.sustech.ces.repository.UserRepository;
import cn.edu.sustech.ces.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class AccountInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${ces.admin.username}")
    private String adminUsername;

    @Value("${ces.admin.password}")
    private String adminPassword;

    @Autowired
    public AccountInitializer(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        UUID id = UUID.fromString("00000000-0000-0000-0000-000000000000");
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isEmpty()) {
            userRepository.createNewUser(id, this.adminUsername, passwordEncoder.encode(this.adminPassword));
            userOptional = userRepository.findById(id);
        }
        User user = userOptional.get();
        user.setNickname(this.adminUsername);
        user.setPassword(passwordEncoder.encode(this.adminPassword));
        user.setPermissionGroup(PermissionGroup.SUPER_ADMIN);
        userRepository.save(user);
    }

}
