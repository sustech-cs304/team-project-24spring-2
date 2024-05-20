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
        for (Map.Entry<UUID, Long> entry : eventRating.entrySet()) {
            System.out.println(entry.getKey() + " " + entry.getValue());
        }

        // 获取所有 event 的 uuid
        List<UUID> eventIDs = eventRating.keySet().stream().toList();
        // 把 event 的 uuid 转换成 index
        Map<UUID, Integer> eventIndex = new HashMap<>();
        for (int i = 0; i < eventIDs.size(); i++) {
            eventIndex.put(eventIDs.get(i), i);
        }

        // 获取两两 event 之间的相似度，用大小为 eventIDs.size() * eventIDs.size() 的矩阵表示
        double[][] similarityMatrix = new double[eventIDs.size()][eventIDs.size()];

        for (int i = 0; i < eventIDs.size(); i++) {
            for (int j = 0; j <= i; j++) {
                if (i == j) {
                    similarityMatrix[i][j] = 1;
                } else {
                    Long intersection = recommendationService.getIntersectCount(eventIDs.get(i), eventIDs.get(j));
                    similarityMatrix[i][j] = intersection / Math.sqrt(eventRating.get(eventIDs.get(i)) * eventRating.get(eventIDs.get(j)));
                    similarityMatrix[j][i] = similarityMatrix[i][j];
                }
            }
        }

        for (int i = 0; i < eventIDs.size(); i++) {
            for (int j = 0; j < eventIDs.size(); j++) {
                System.out.print(similarityMatrix[i][j] + " ");
            }
            System.out.println();
        }

        // 获取用户推荐的 event
        // 找到当前用户有评分的所有 event
        List<Recommendation> recommendations = recommendationService.getRecommendationsByUserID(userID);
        List<UUID> ratedEventIDs = recommendations.stream().map(recommendation -> recommendation.getRecommend_id().getEvent_id()).toList();
        // 使用 eventIndex 找到对应的 index
        List<Integer> ratedEventIndex = ratedEventIDs.stream().map(eventIndex::get).toList();
        Map<UUID, Double> recommendationMap = new HashMap<>();
        for (int i = 0; i < eventIDs.size(); i++) {
            UUID eventUUID = eventIDs.get(i);
            double rating = 0;
            for (int j = 0; j < ratedEventIndex.size(); j++) {
                int ratedIndex = ratedEventIndex.get(j);
                rating += similarityMatrix[i][ratedIndex] * recommendations.get(j).getValue();
            }
            recommendationMap.put(eventUUID, rating);
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("userID", userID);
        jsonObject.put("recommendations", recommendationMap);
        return ResponseEntity.ok(jsonObject);
    }
}
