package cn.edu.sustech.ces.controller;

import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.client.HttpClientErrorException;

import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    // 模拟登录并获取JWT令牌
    private String obtainAccessToken(String username, String password) throws Exception {
        // 模拟登录
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String jsonRequest = "{\"user_input\":\"" + username + "\",\"password\":\"" + password + "\"}";

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/user/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        String response = result.getResponse().getContentAsString();
        return JsonPath.parse(response).read("$.access_token", String.class);
    }
    @Test
    void testLoginFailed() throws Exception {
        String jsonRequest = "{\"user_input\":\"admin\",\"password\":\"admin1\"}";

        try {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/user/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(MockMvcResultMatchers.status().isInternalServerError())
                .andReturn();
        } catch (HttpClientErrorException e) {
            assertEquals(e.getRawStatusCode(), 500);
        }
    }

    // 测试获取个人资料的方法
    @Test
    void testFetchProfile() throws Exception {
        // 先获取访问令牌
        String accessToken = obtainAccessToken("admin", "admin");

        // 使用获取的令牌调用受保护的API
        mockMvc.perform(MockMvcRequestBuilders.post("/api/user/profile")
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists());
    }

    @Test
    void testVerifyLoginState() throws Exception {
        String accessToken = obtainAccessToken("admin", "admin");

        mockMvc.perform(MockMvcRequestBuilders.post("/api/user/verify")
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Verified"));
    }

    @Test
    void testGetUser() throws Exception {
        String accessToken = obtainAccessToken("admin", "admin");
        String validUserId = "00000000-0000-0000-0000-000000000000";

        mockMvc.perform(MockMvcRequestBuilders.post("/api/user/get-user?userId=" + validUserId)
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(notNullValue()));
    }

    @Test
    void testListUserSize() throws Exception {
        String accessToken = obtainAccessToken("admin", "admin");

        mockMvc.perform(MockMvcRequestBuilders.post("/api/user/list-user-size")
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void testListUser() throws Exception {
        String accessToken = obtainAccessToken("admin", "admin");

        mockMvc.perform(MockMvcRequestBuilders.post("/api/user/list-user?page=0&size=10")
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void testRegisterWithInvalidCode() throws Exception {
        String accessToken = obtainAccessToken("admin", "admin");
        mockMvc.perform(MockMvcRequestBuilders.post("/api/user/fetch-register-code?email=test@example.com")
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(MockMvcResultMatchers.status().isOk());

        String jsonRequest = "{\"username\":\"testUser\",\"realName\":\"Real Name\",\"email\":\"test@example.com\",\"password\":\"securePassword123\",\"phone\":\"1234567890\",\"code\":\"1234\"}";

        mockMvc.perform(MockMvcRequestBuilders.post("/api/user/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(result -> assertEquals(result.getResponse().getContentAsString(), "Invalid Code"));
    }

    @Test
    void testUpdateProfile() throws Exception {
        String accessToken = obtainAccessToken("admin", "admin");
        String jsonRequest = "{\"gender\":\"MALE\",\"birthday\":1627814400000,\"description\":\"Updated description\",\"password\":\"admin\",\"phone\":\"1234567899\"}";

        mockMvc.perform(MockMvcRequestBuilders.post("/api/user/update-profile")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest)
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void testListOrders() throws Exception {
        String accessToken = obtainAccessToken("admin", "admin");

        mockMvc.perform(MockMvcRequestBuilders.post("/api/user/list-orders")
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void testListTickets() throws Exception {
        String accessToken = obtainAccessToken("admin", "admin");

        mockMvc.perform(MockMvcRequestBuilders.post("/api/user/list-tickets")
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
    @Test
    void testChangePermission() throws Exception {
        String accessToken = obtainAccessToken("admin", "admin");
        mockMvc.perform(MockMvcRequestBuilders.post("/api/user/change-permission?userId=00000000-0000-0000-0000-000000000000&permissionGroup=SUPER_ADMIN")
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    void testGetUploadImages() throws Exception {
        String accessToken = obtainAccessToken("admin", "admin");

        mockMvc.perform(MockMvcRequestBuilders.post("/api/user/get-upload-images")
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}



