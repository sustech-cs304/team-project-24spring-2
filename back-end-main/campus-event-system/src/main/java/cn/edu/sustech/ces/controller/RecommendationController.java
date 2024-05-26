package cn.edu.sustech.ces.controller;

import cn.edu.sustech.ces.entity.Recommendation;
import cn.edu.sustech.ces.entity.RecommendationPK;

import cn.edu.sustech.ces.service.RecommendationService;
import com.alibaba.fastjson.JSONObject;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<?> addRecommendation(
            @RequestParam UUID userID,
            @RequestParam UUID eventID,
            @RequestParam Long rating
    ) {
        RecommendationPK recommendationPK = new RecommendationPK();
        recommendationPK.setUserId(userID);
        recommendationPK.setEventId(eventID);

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
    public ResponseEntity<?> deleteRecommendation(
            @RequestParam UUID userID,
            @RequestParam UUID eventID
    ) {
        recommendationService.deleteRecommendation(userID, eventID);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("userID", userID);
        jsonObject.put("eventID", eventID);
        return ResponseEntity.ok(jsonObject);
    }

    @PostMapping("/update")
    public ResponseEntity<?> updateRecommendation(
            @RequestParam UUID userID,
            @RequestParam UUID eventID,
            @RequestParam Long rating
    ) {
        recommendationService.deleteRecommendation(userID, eventID);
        return addRecommendation(userID, eventID, rating);
    }

    @PostMapping("/get")
    public ResponseEntity<?> getRecommendation(@RequestParam UUID userID) {
        Map<UUID, Long> eventRating = recommendationService.getEventRating();
        List<UUID> eventIDs = eventRating.keySet().stream().toList();
        Map<UUID, Integer> eventIndex = new HashMap<>();
        for (int i = 0; i < eventIDs.size(); i++) {
            eventIndex.put(eventIDs.get(i), i);
        }
        double[][] similarityMatrix = new double[eventIDs.size()][eventIDs.size()];
        for (int i = 0; i < eventIDs.size(); i++) {
            for (int j = 0; j <= i; j++) {
                if (i == j) {
                    similarityMatrix[i][j] = 1;
                } else {
                    Long intersection = recommendationService.getIntersectCount(
                        eventIDs.get(i),
                        eventIDs.get(j)
                    );
                    similarityMatrix[i][j] = intersection / Math.sqrt(
                        eventRating.get(eventIDs.get(i)) * eventRating.get(eventIDs.get(j))
                    );
                    similarityMatrix[j][i] = similarityMatrix[i][j];
                }
            }
        }
        List<Recommendation> recommendations =
                recommendationService.getRecommendationsByUserID(userID);
        List<UUID> ratedEventIDs = recommendations.stream().map(
                recommendation -> recommendation.getRecommendId().getEventId()
        ).toList();
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
