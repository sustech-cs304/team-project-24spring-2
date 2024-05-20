package cn.edu.sustech.ces.controller;

import cn.edu.sustech.ces.entity.Recommendation;
import cn.edu.sustech.ces.entity.RecommendationPK;
import cn.edu.sustech.ces.service.EventService;

import cn.edu.sustech.ces.service.RecommendationService;
import com.alibaba.fastjson.JSONObject;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/recommend")
@AllArgsConstructor
public class RecommendationController {
    private final RecommendationService recommendationService;
    @PostMapping("/add")
    public ResponseEntity<?> add_recommendation(@RequestParam UUID userID, @RequestParam UUID eventID, @RequestParam Long rating) {
        RecommendationPK recommendationPK = new RecommendationPK();
        recommendationPK.setUser_id(userID);
        recommendationPK.setEvent_id(eventID);

        Recommendation recommendation = new Recommendation(
                recommendationPK,
                rating
        );

        recommendationService.addRecommendation(recommendation);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("userID", userID);
        jsonObject.put("eventID", eventID);
        jsonObject.put("rating", rating);

        return ResponseEntity.ok(jsonObject);
    }
    @PostMapping("/delete")
    public ResponseEntity<?> delete_recommendation(@RequestParam UUID userID, @RequestParam UUID eventID) {
        recommendationService.deleteRecommendation(userID, eventID);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("userID", userID);
        jsonObject.put("eventID", eventID);
        return ResponseEntity.ok(jsonObject);
    }

    @PostMapping("/update")
    public ResponseEntity<?> update_recommendation(@RequestParam UUID userID, @RequestParam UUID eventID, @RequestParam Long rating) {
        recommendationService.deleteRecommendation(userID, eventID);
        return add_recommendation(userID, eventID, rating);
    }

    @PostMapping("/get")
    public ResponseEntity<?> get_recommendation(@RequestParam UUID userID) {
        // 获取 event, 有多少用户推荐了这个 event
        Map<UUID, Long> eventRating = recommendationService.getEventRating();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("userID", userID);
        jsonObject.put("eventRating", eventRating);
        return ResponseEntity.ok(jsonObject);
    }
}
