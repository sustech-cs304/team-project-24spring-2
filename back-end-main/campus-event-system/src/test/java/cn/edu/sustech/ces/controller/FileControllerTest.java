package cn.edu.sustech.ces.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.junit.jupiter.api.BeforeEach;
import java.util.UUID;

@SpringBootTest
@AutoConfigureMockMvc
public class FileControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private String accessToken;
    private UUID testEventId;
    private UUID testCommentId;

    @BeforeEach
    public void setup() throws Exception {
        accessToken = obtainAccessToken("admin", "admin");
        testEventId = createTestEvent(accessToken);  // Create a test event
        testCommentId = createTestComment(accessToken, testEventId);  // Create a test comment
    }

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

        return JsonPath.read(result.getResponse().getContentAsString(), "$.access_token");
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
        String jsonRequest = "{\"event_id\":\"" + eventId + "\",\"content\":\"Test comment\"}";
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/comment/make-comment")
                        .header("Authorization", "Bearer " + accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk())
                .andReturn();

        return UUID.fromString(JsonPath.read(result.getResponse().getContentAsString(), "$.id"));
    }

    @Test
    public void testUploadFileForComment() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "test.png", MediaType.IMAGE_PNG_VALUE, "Image Content".getBytes());

        mockMvc.perform(multipart("/api/file/upload").file(file)
                        .param("usage", "comment")
                        .param("commentId", testCommentId.toString())
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk());
    }

    @Test
    public void testUploadEventFile() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "test.png", MediaType.IMAGE_PNG_VALUE, "Image Content".getBytes());

        mockMvc.perform(multipart("/api/file/upload").file(file)
                        .param("usage", "event")
                        .param("eventId", testEventId.toString())
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk());
    }
    @Test
    public void testUploadFileForUser() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "test.png", MediaType.IMAGE_PNG_VALUE, "Image Content".getBytes());

        mockMvc.perform(multipart("/api/file/upload").file(file)
                        .param("usage", "user")
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk());
    }
    @Test
    public void testUploadFileForAvatar() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "test.png", MediaType.IMAGE_PNG_VALUE, "Image Content".getBytes());

        mockMvc.perform(multipart("/api/file/upload").file(file)
                        .param("usage", "avatar")
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk());
    }
    @Test
    public void testDeleteUserFile() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/file/delete")
                        .param("usage", "user")
                        .param("fileName", "avatar.png")
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk())
                .andExpect(content().string("File deleted"));
    }
}
