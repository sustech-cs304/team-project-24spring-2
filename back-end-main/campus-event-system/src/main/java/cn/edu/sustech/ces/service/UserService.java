package cn.edu.sustech.ces.service;

import cn.edu.sustech.ces.entity.*;
import cn.edu.sustech.ces.enums.PermissionGroup;
import cn.edu.sustech.ces.repository.UserRepository;
import cn.edu.sustech.ces.security.CESUserDetails;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
@AllArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;


    public User registerUser(String nickname, String realName, String description,
                             String email, String encryptedPassword, String phone,
                             PermissionGroup group) {
        User user = new User();
        user.setNickname(nickname);
        user.setRealName(realName);
        user.setDescription(description);
        user.setEmail(email);
        user.setPassword(encryptedPassword);
        user.setPhone(phone);
        user.setPermissionGroup(group);
        userRepository.save(user);
        return user;
    }


    public User updateUser(User user) {
        user = userRepository.save(user);
        return user;
    }

    // TODO: encrypt password

    public User changeUserPassword(UUID userId, String newPassword) {
        User user = userRepository.findById(userId).orElse(null);
        if (user != null) {
            return changeUserPassword(user, newPassword);
        }
        return null;
    }

    public User changeUserPassword(User user, String newPassword) {

        user.setPassword(newPassword);
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
        }
        if (user == null && userInput.matches("[0-9]+")) {
            user = getUserByPhone(userInput);
        }
        if (user == null) {
            user = getUserByNickname(userInput);
        }
        if (user == null) {
            throw new UsernameNotFoundException("User not found with input: " + userInput);
        }
        return new CESUserDetails(user);
    }

    public List<User> listUser(String nickname, String email) {
        List<User> user = userRepository.findAll();
        return user.stream().filter(u -> {
            if (nickname != null && !u.getNickname().contains(nickname)) {
                return false;
            }
            if (email != null && !u.getEmail().equals(email)) {
                return false;
            }
            return true;
        }).toList();
    }

    public Long listUserSize(String nickname, String email) {
        return (long) listUser(nickname, email).size();
    }
}
