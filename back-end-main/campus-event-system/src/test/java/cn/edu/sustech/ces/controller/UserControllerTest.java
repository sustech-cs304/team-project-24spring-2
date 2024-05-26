package cn.edu.sustech.ces.controller;

import cn.edu.sustech.ces.CampusEventSystemApplication;
import cn.edu.sustech.ces.enums.PermissionGroup;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import com.alibaba.fastjson.JSONObject;
import org.springframework.web.util.UriComponentsBuilder;

import static org.junit.jupiter.api.Assertions.*;

public class UserControllerTest {
    private final String baseUrl = "http://localhost:8080/api/user";
    private final RestTemplate restTemplate = new RestTemplate();

    private static ConfigurableApplicationContext context;

    @AfterAll
    static void tearDownAfterClass() {
        // 关闭Spring应用上下文
        if (context != null) {
            context.close();
        }
    }

    // 创建配置了 JSON 消息转换器的 RestTemplate
    private RestTemplate createRestTemplateWithJsonConverter() {
        RestTemplate restTemplate = new RestTemplate();

        // Find and replace MappingJackson2HttpMessageConverter to ensure it's correctly added
        List<HttpMessageConverter<?>> messageConverters = restTemplate.getMessageConverters();
        boolean replaced = false;
        for (int i = 0; i < messageConverters.size(); i++) {
            if (messageConverters.get(i) instanceof MappingJackson2HttpMessageConverter) {
                messageConverters.set(i, new MappingJackson2HttpMessageConverter());
                replaced = true;
                break;
            }
        }
        if (!replaced) {
            messageConverters.add(new MappingJackson2HttpMessageConverter());
        }

        return restTemplate;
    }

    // 修改 getAccessToken 方法以使用上面创建的 RestTemplate
    private String getAccessToken() {
        //等待1秒钟
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        RestTemplate restTemplate = createRestTemplateWithJsonConverter();
        String loginUrl = baseUrl + "/login";
        LoginRequest loginRequest = new LoginRequest("admin", "admin");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<LoginRequest> entity = new HttpEntity<>(loginRequest, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(loginUrl, entity, String.class);

        // 使用 fastjson 解析 JSON 字符串
        JSONObject jsonResponse = JSONObject.parseObject(response.getBody());
        return jsonResponse.getString("access_token");  // 从 JSON 对象中获取 "access_token" 字段
    }

    private HttpHeaders getHeadersWithToken(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.setBearerAuth(token);
        return headers;
    }
    @Test
    void testFetchProfile() {
        String url = baseUrl + "/profile";
        HttpEntity<?> entity = new HttpEntity<>(getHeadersWithToken(getAccessToken()));
        ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(JSONObject.parseObject(response.getBody()).getString("nickname"));
    }

    @Test
    void testVerifyLoginState() {
        String url = baseUrl + "/verify";
        HttpEntity<?> entity = new HttpEntity<>(getHeadersWithToken(getAccessToken()));
        ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Verified", response.getBody());
    }

    @Test
    void testGetUser() {
        // 假设这里的 UUID 是已经存在的有效用户ID
        String validUserId = "00000000-0000-0000-0000-000000000000"; // 替换为实际的UUID
        String url = baseUrl + "/get-user?userId=" + validUserId;
        HttpEntity<?> entity = new HttpEntity<>(getHeadersWithToken(getAccessToken()));
        ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void testListUserSize() {
        String url = baseUrl + "/list-user-size";
        HttpEntity<?> entity = new HttpEntity<>(getHeadersWithToken(getAccessToken()));
        ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testListUser() {
        String url = baseUrl + "/list-user?page=0&size=10";
        HttpEntity<?> entity = new HttpEntity<>(getHeadersWithToken(getAccessToken()));
        ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testRegisterWithInvalidCode() {
        // 获取验证码
        String email = "test@example.com";
        String fetchCodeUrl = baseUrl + "/fetch-register-code?email=" + email;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<?> fetchCodeEntity = new HttpEntity<>(headers);
        ResponseEntity<String> fetchCodeResponse = restTemplate.postForEntity(fetchCodeUrl, fetchCodeEntity, String.class);


        String code = "1234";
        String registerUrl = baseUrl + "/register";
        RegisterRequest registerRequest = new RegisterRequest("testUser", "Real Name", email, "securePassword123", "1234567890", code);
        HttpEntity<RegisterRequest> registerEntity = new HttpEntity<>(registerRequest, headers);

        try{
            ResponseEntity<String> registerResponse = restTemplate.postForEntity(registerUrl, registerEntity, String.class);
        } catch (HttpClientErrorException e) {
            assertTrue(e.getStatusCode() == HttpStatus.BAD_REQUEST || e.getStatusCode() == HttpStatus.TOO_MANY_REQUESTS,
                    "Expected status code 400 or 429, but got: " + e.getStatusCode());
        }
        // 检查注册结果

    }


    @Test
    void testUpdateProfile() {
        String url = baseUrl + "/update-profile";
        JSONObject requestBody = new JSONObject();
        requestBody.put("gender", "MALE");
        requestBody.put("birthday", 1627814400000L); // 示例日期，如2021-08-01
        requestBody.put("description", "Updated description");
        requestBody.put("password", "admin");
        requestBody.put("phone", "1234567890");

        HttpHeaders headers = getHeadersWithToken(getAccessToken());
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(requestBody.toString(), headers);

        ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    // 测试获取用户订单列表
    @Test
    void testListOrders() {
        String url = baseUrl + "/list-orders";
        HttpEntity<?> entity = new HttpEntity<>(getHeadersWithToken(getAccessToken()));
        ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    // 测试获取用户票据列表
    @Test
    void testListTickets() {
        String url = baseUrl + "/list-tickets";
        HttpEntity<?> entity = new HttpEntity<>(getHeadersWithToken(getAccessToken()));
        ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    // 测试更改用户权限
    @Test
    void testChangePermission() {
        // 服务器端接受@RequestParam，我们需要在URL中明确地传递它们
        String baseUrl = "http://localhost:8080/api/user/change-permission";
        UUID userId = UUID.fromString("00000000-0000-0000-0000-000000000000");
        PermissionGroup newPermissionGroup = PermissionGroup.SUPER_ADMIN;

        // 构建带参数的URL
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(baseUrl)
                .queryParam("userId", userId.toString())
                .queryParam("permissionGroup", newPermissionGroup.name());

        HttpHeaders headers = getHeadersWithToken(getAccessToken());
        HttpEntity<?> entity = new HttpEntity<>(headers);

        // 使用exchange方法发送POST请求
        try {
            ResponseEntity<String> response =  restTemplate.postForEntity(builder.toUriString(), entity, String.class);
        } catch (HttpClientErrorException e) {
            assertEquals(HttpStatus.FORBIDDEN, e.getStatusCode());
        }
    }

    // 测试获取用户上传的图片列表
    @Test
    void testGetUploadImages() {
        String url = baseUrl + "/get-upload-images";
        HttpEntity<?> entity = new HttpEntity<>(getHeadersWithToken(getAccessToken()));
        ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }


    // 登录请求体的静态内部类
    static class LoginRequest {
        private String userInput;
        private String password;

        public LoginRequest(String userInput, String password) {
            this.userInput = userInput;
            this.password = password;
        }

        public String getUserInput() {
            return userInput;
        }

        public void setUserInput(String userInput) {
            this.userInput = userInput;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }

    // 注册请求体的静态内部类
    static class RegisterRequest {
        @JsonProperty("nickname")
        private String nickname;
        @JsonProperty("realName")
        private String realName;
        @JsonProperty("email")
        private String email;
        @JsonProperty("password")
        private String password;
        @JsonProperty("phone")
        private String phone;
        @JsonProperty("verifyCode")
        private String verifyCode;

        RegisterRequest(String nickname, String realName, String email, String password, String phone, String verifyCode) {
            this.nickname = nickname;
            this.realName = realName;
            this.email = email;
            this.password = password;
            this.phone = phone;
            this.verifyCode = verifyCode;
        }
    }
}
