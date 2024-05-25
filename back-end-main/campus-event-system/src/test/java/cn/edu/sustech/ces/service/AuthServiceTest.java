package cn.edu.sustech.ces.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import cn.edu.sustech.ces.security.JwtTokenProvider;
import cn.edu.sustech.ces.security.LoginRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthService authService;

    @Test
    void testLogin() {
        LoginRequest request = new LoginRequest("user", "password");

        Authentication authentication = mock(Authentication.class);
        when(authenticationManager.authenticate(any(Authentication.class)))
                .thenReturn(authentication);

        String expectedToken = "token";
        when(jwtTokenProvider.generateToken(authentication)).thenReturn(expectedToken);

        String token = authService.login(request);

        assertEquals(expectedToken, token);

        verify(authenticationManager).authenticate(any(Authentication.class));
        verify(jwtTokenProvider).generateToken(authentication);
    }
}
