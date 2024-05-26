package cn.edu.sustech.ces.controller;

import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.UUID;


import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@SpringBootTest
@AutoConfigureMockMvc
public class CommentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private String accessToken;
    private UUID testEventId;
    private UUID testCommentId;
    private String obtainAccessToken(String username, String password) throws Exception {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String jsonRequest = "{\"user_input\":\"" + username + "\",\"password\":\"" + password + "\"}";

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/user/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk())
                .andReturn();

        String response = result.getResponse().getContentAsString();
        return JsonPath.parse(response).read("$.access_token", String.class);
    }
    @BeforeEach
    public void setup() throws Exception {
        // 模拟登录获取accessToken
        this.accessToken = obtainAccessToken("admin", "admin");
        // 创建测试用事件
        this.testEventId = createTestEvent(accessToken);
        // 创建测试用评论
        this.testCommentId = createTestComment(accessToken, testEventId);
    }

    private UUID createTestEvent(String accessToken) throws Exception {
        // 模拟创建事件的请求，确保在创建评论前有一个有效的事件存在
        // 返回事件的ID
        String jsonRequest = "{\"title\":\"New Event\",\"start_time\":1627814400000,\"end_time\":1627914400000,\"document_url\":\"http://example.com/doc.pdf\",\"image_url\":\"http://example.com/image.jpg\",\"latitude\":34.0522,\"longitude\":118.2437,\"location_name\":\"Los Angeles\",\"category\":\"Conference\",\"tickets\":[{\"description\":\"General Admission\",\"price\":100.00,\"total_amount\":150}]}";

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/event/create-event")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest)
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk())
                .andReturn();

        String response = result.getResponse().getContentAsString();
        return UUID.fromString(JsonPath.parse(response).read("$.id", String.class));
    }

    private UUID createTestComment(String accessToken, UUID eventId) throws Exception {
        String jsonRequest = "{\"event_id\":\"" + testEventId + "\",\"content\":\"This is a test comment.\"}";
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/comment/make-comment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest)
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").value("This is a test comment."))
                .andReturn();
        String response = result.getResponse().getContentAsString();
        return UUID.fromString(JsonPath.parse(response).read("$.id", String.class));
    }

    @Test
    public void testMakeComment() throws Exception {
        String jsonRequest = "{\"event_id\":\"" + testEventId + "\",\"content\":\"This is a test comment.\"}";
        mockMvc.perform(MockMvcRequestBuilders.post("/api/comment/make-comment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest)
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").value("This is a test comment."));
    }

    @Test
    public void testDeleteComment() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/comment/delete-comment")
                        .param("commentId", testCommentId.toString())
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetComment() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/comment/get-comment")
                        .param("commentId", testCommentId.toString())
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(testCommentId.toString()));
    }

    @Test
    public void testGetCommentAttachments() throws Exception {
        // 假设您已经为特定评论上传了附件
        mockMvc.perform(MockMvcRequestBuilders.post("/api/comment/get-comment-attachments")
                        .param("commentId", testCommentId.toString())
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }
}