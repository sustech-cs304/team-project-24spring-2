package cn.edu.sustech.ces.controller;
import cn.edu.sustech.ces.enums.EventStatus;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import java.util.UUID;

import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class EventControllerTest {

    @Autowired
    private MockMvc mockMvc;

    // 获取访问令牌的辅助方法
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

    // 创建事件并返回事件ID
    private UUID createEvent(String accessToken) throws Exception {
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

    private UUID createEvent(String title, EventStatus status, String category, String accessToken) throws Exception {
        JSONObject eventDetails = new JSONObject();
        eventDetails.put("title", title);
        eventDetails.put("start_time", System.currentTimeMillis() + 100000);
        eventDetails.put("end_time", System.currentTimeMillis() + 200000);
        eventDetails.put("document_url", "http://example.com/doc.pdf");
        eventDetails.put("image_url", "http://example.com/image.jpg");
        eventDetails.put("latitude", 34.0522);
        eventDetails.put("longitude", -118.2437);
        eventDetails.put("location_name", "Los Angeles");
        eventDetails.put("category", category);
        eventDetails.put("status", status.toString());
        eventDetails.put("tickets", JSONArray.parse("[{\"description\":\"General Admission\",\"price\":100.00,\"total_amount\":150}]"));

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/event/create-event")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(eventDetails.toJSONString())
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk())
                .andReturn();

        String response = result.getResponse().getContentAsString();
        return UUID.fromString(JsonPath.parse(response).read("$.id", String.class));
    }
    // 测试更新事件
    @Test
    public void testUpdateAndDeleteEvent() throws Exception {
        String accessToken = obtainAccessToken("admin", "admin");
        UUID eventId = createEvent(accessToken);  // 先创建事件

        JSONObject updateRequest = new JSONObject();
        updateRequest.put("title", "Updated Title");
        updateRequest.put("start_time", System.currentTimeMillis() + 100000);
        updateRequest.put("end_time", System.currentTimeMillis() + 200000);
        updateRequest.put("document_url", "http://example.com/updated_doc.pdf");
        updateRequest.put("image_url", "http://example.com/updated_image.jpg");
        updateRequest.put("latitude", 35.0522);
        updateRequest.put("longitude", 119.2437);
        updateRequest.put("location_name", "Updated Location");
        updateRequest.put("category", "Updated Category");
        updateRequest.put("tickets", JSONArray.parse("[{\"description\":\"Premium\",\"price\":150.00,\"total_amount\":50,\"sold_amount\":0}]"));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/event/update-event")
                        .param("eventId", eventId.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateRequest.toJSONString())
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated Title"))
                .andExpect(jsonPath("$.document_url").value("http://example.com/updated_doc.pdf"))
                .andExpect(jsonPath("$.image_url").value("http://example.com/updated_image.jpg"))
                .andExpect(jsonPath("$.latitude").value(35.0522))
                .andExpect(jsonPath("$.longitude").value(119.2437))
                .andExpect(jsonPath("$.location_name").value("Updated Location"))
                .andExpect(jsonPath("$.category").value("Updated Category"));

        // 删除事件
        mockMvc.perform(MockMvcRequestBuilders.post("/api/event/delete-event")
                        .param("eventId", eventId.toString())
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk());

        mockMvc.perform(MockMvcRequestBuilders.post("/api/event/get-event")
                .param("eventId", eventId.toString())
                .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isBadRequest());
    }

    // 测试发布事件
    @Test
    public void testPublishAndAuditAndGetEvent() throws Exception {
        String accessToken = obtainAccessToken("admin", "admin");
        UUID eventId = createEvent(accessToken);  // 先创建事件

        // 获取单个事件
        mockMvc.perform(MockMvcRequestBuilders.post("/api/event/get-event")
                        .param("eventId", eventId.toString())
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk());
        String jsonRequest = "{\"pass\":true}";
        mockMvc.perform(MockMvcRequestBuilders.post("/api/event/audit-event")
                        .param("eventId", eventId.toString())
                        .param("pass", "true")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest)
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isBadRequest());

        // 发布事件
        mockMvc.perform(MockMvcRequestBuilders.post("/api/event/publish-event")
                        .param("eventId", eventId.toString())
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk());

        // 获取单个事件
        mockMvc.perform(MockMvcRequestBuilders.post("/api/event/get-event")
                        .param("eventId", eventId.toString())
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk());
    }

    @Test
    public void testExploreEventsSize() throws Exception {
        String accessToken = obtainAccessToken("admin", "admin");
        createEvent("Event_Music", EventStatus.PENDING, "Music", accessToken);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/event/explore-events-size")
                        .param("title", "Event_Music")
                        .param("category", "Music")
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk())
                .andReturn();
        assertTrue(Integer.parseInt(result.getResponse().getContentAsString()) == 0);
    }

    @Test
    public void testExploreEvents() throws Exception {
        String accessToken = obtainAccessToken("admin", "admin");

        mockMvc.perform(MockMvcRequestBuilders.post("/api/event/explore-events")
                        .param("page", "0")
                        .param("size", "10")
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk());
    }

    @Test
    public void testListEventsSize() throws Exception {
        String accessToken = obtainAccessToken("admin", "admin");
        createEvent("Event 3", EventStatus.EDITING, "Seminar", accessToken);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/event/list-events-size")
                        .param("title", "Event")
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk())
                .andReturn();

        assertTrue(Integer.parseInt(result.getResponse().getContentAsString()) > 0);
    }

    @Test
    public void testListEvents() throws Exception {
        String accessToken = obtainAccessToken("admin", "admin");

        mockMvc.perform(MockMvcRequestBuilders.post("/api/event/list-events")
                        .param("page", "0")
                        .param("size", "10")
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(1))));
    }



}
