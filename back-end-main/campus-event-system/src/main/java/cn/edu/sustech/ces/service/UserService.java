package cn.edu.sustech.ces.service;

import cn.edu.sustech.ces.entity.Location;
import cn.edu.sustech.ces.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.edu.sustech.ces.entity.User;

import java.util.UUID;


@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User getUserById(UUID id) {
        return userRepository.findById(id).orElse(null);
    }
}
