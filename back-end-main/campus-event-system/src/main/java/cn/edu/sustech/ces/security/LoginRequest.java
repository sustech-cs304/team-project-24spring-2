package cn.edu.sustech.ces.security;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class LoginRequest {
    private String userInput;
    private String password;
}
