package cn.edu.sustech.ces.security;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class RegisterRequest {
    private String nickname;
    private String password;
    private String email;
    private String phone;
    private String realName;
    private String verifyCode;
}
