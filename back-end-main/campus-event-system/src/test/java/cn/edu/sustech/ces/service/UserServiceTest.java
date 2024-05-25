package cn.edu.sustech.ces.service;

import cn.edu.sustech.ces.entity.User;
import cn.edu.sustech.ces.enums.PermissionGroup;
import cn.edu.sustech.ces.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void registerUserTest() {
        User user = new User();
        user.setNickname("testUser");
        when(userRepository.save(any(User.class))).thenReturn(user);

        User result = userService.registerUser("testUser", "Real Name", "Desc",
                "email@example.com", "encryptedPassword", "1234567890",
                PermissionGroup.USER);

        verify(userRepository).save(any(User.class));
        assertEquals("testUser", result.getNickname());
    }

    @Test
    void updateUserTest() {
        User user = new User();
        user.setNickname("testUser");
        when(userRepository.save(any(User.class))).thenReturn(user);

        User updatedUser = userService.updateUser(user);

        verify(userRepository).save(user);
        assertEquals("testUser", updatedUser.getNickname());
    }

    @Test
    void getUserByIdTest() {
        UUID userId = UUID.randomUUID();
        User user = new User();
        user.setId(userId);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        User found = userService.getUserById(userId);

        verify(userRepository).findById(userId);
        assertNotNull(found);
    }

    @Test
    void loadUserByUsernameTest() {
        String nickname = "testUser";
        User user = new User();
        user.setNickname(nickname);
        user.setEmail("email@example.com");
        when(userRepository.findByNickname(nickname)).thenReturn(Collections.singletonList(user));

        assertDoesNotThrow(() -> {
            userService.loadUserByUsername(nickname);
        });

        verify(userRepository).findByNickname(nickname);
    }

    @Test
    void loadUserByUsernameNotFoundTest() {
        when(userRepository.findByEmail(anyString())).thenReturn(Collections.emptyList());
        when(userRepository.findByPhone(anyString())).thenReturn(Collections.emptyList());
        when(userRepository.findByNickname(anyString())).thenReturn(Collections.emptyList());

        assertThrows(UsernameNotFoundException.class, () -> {
            userService.loadUserByUsername("nonexistent");
        });
    }

}
