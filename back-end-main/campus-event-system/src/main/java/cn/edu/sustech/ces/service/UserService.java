package cn.edu.sustech.ces.service;

import cn.edu.sustech.ces.entity.*;
import cn.edu.sustech.ces.enums.PermissionGroup;
import cn.edu.sustech.ces.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.parameters.P;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;


@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User registerUser(String nickname, String realName, String description,
                             String email, String password, String phone,
                             PermissionGroup group) {
        User user = new User();
        user.setNickname(nickname);
        user.setRealName(realName);
        user.setDescription(description);
        user.setEmail(email);
        user.setPassword(password);
        user.setPhone(phone);
        user.setPermissionGroup(group);
        userRepository.save(user);
        return user;
    }

    public User getUserById(UUID userId) {
        return userRepository.findById(userId).orElse(null);
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email).stream().findFirst().orElse(null);
    }

    public User getUserByNickname(String nickname) {
        return userRepository.findByNickname(nickname).stream().findFirst().orElse(null);
    }

    public User getUserByPhone(String phone) {
        return userRepository.findByPhone(phone).stream().findFirst().orElse(null);
    }

    public List<User> getUsersByRealName(String realName) {
        return userRepository.findByRealName(realName);
    }

    @Override
    public UserDetails loadUserByUsername(String userInput) throws UsernameNotFoundException {
        User user = null;
        if (userInput.contains("@")) {
            user = getUserByEmail(userInput);
        } else {
            if (userInput.matches("[0-9]+")) {
                user = getUserByPhone(userInput);
            } else {
                user = getUserByNickname(userInput);
            }
        }
        if (user == null) {
            throw new UsernameNotFoundException("User not found with input: " + userInput);
        }
        return new org.springframework.security.core.userdetails.User(
                userInput,
                user.getPassword(),
                Set.of(new SimpleGrantedAuthority(user.getPermissionGroup().name()))
        );
    }
}
