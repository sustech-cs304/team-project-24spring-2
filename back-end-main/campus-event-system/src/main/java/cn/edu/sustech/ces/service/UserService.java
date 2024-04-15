package cn.edu.sustech.ces.service;

import cn.edu.sustech.ces.entity.*;
import cn.edu.sustech.ces.repository.UserCreateEventRepository;
import cn.edu.sustech.ces.repository.UserEnrollEventRepository;
import cn.edu.sustech.ces.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;


@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserCreateEventRepository userCreateEventRepository;
    @Autowired
    private UserEnrollEventRepository userEnrollEventRepository;


    public User getUserById(UUID id) {
        return userRepository.findById(id).orElse(null);
    }

    public User deleteUserById(UUID id) {
        User user = userRepository.findById(id).orElse(null);
        if (user != null) {
            userRepository.deleteById(id);
        }
        return user;
    }

    public List<User> searchUsersByField(String field, String value) {
        switch (field.toLowerCase()) {
            case "id":
                return List.of(Objects.requireNonNull(userRepository.findById(UUID.fromString(value)).orElse(null)));
            case "realname":
                return userRepository.findByRealname(value);
            case "email":
                return userRepository.findByEmail(value);
            case "nickname":
                return userRepository.findByNickname(value);
            case "phone":
                return userRepository.findByPhone(value);
            default:
                throw new IllegalArgumentException("Invalid field for search");
        }
    }

    public List<Event> findEventsByUser(String type, UUID userId) {
        if (type.equalsIgnoreCase("created")) {
            return userCreateEventRepository.findByUserId(userId).stream()
                    .map(UserCreateEvent::getEvent)
                    .collect(Collectors.toList());
        } else if (type.equalsIgnoreCase("enrolled")) {
            return userEnrollEventRepository.findByUserId(userId).stream()
                    .map(UserEnrollEvent::getEvent)
                    .collect(Collectors.toList());
        }
        return List.of(); // 返回空列表或抛出异常，如果类型不匹配
    }
}
