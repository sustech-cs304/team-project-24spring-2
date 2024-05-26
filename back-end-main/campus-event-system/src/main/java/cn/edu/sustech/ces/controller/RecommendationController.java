package cn.edu.sustech.ces.controller;

import cn.edu.sustech.ces.entity.Recommendation;
import cn.edu.sustech.ces.entity.RecommendationPK;

import cn.edu.sustech.ces.entity.User;
import cn.edu.sustech.ces.service.RecommendationService;
import cn.edu.sustech.ces.utils.CESUtils;
import com.alibaba.fastjson.JSONObject;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/recommend")
@AllArgsConstructor
public class RecommendationController {

    private final RecommendationService recommendationService;

    @PostMapping("/add")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> addRecommendation(
            @RequestParam UUID eventId
    ) {
        User user = CESUtils.getAuthorizedUser();
        return ResponseEntity.ok(recommendationService.addRecommendation(user.getId(), eventId, 1L));
    }

    @PostMapping("/get")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getRecommendation(
            @RequestParam UUID eventId
    ) {
        User user = CESUtils.getAuthorizedUser();
        RecommendationPK recommendationPK = new RecommendationPK(user.getId(), eventId);
        Recommendation rec = recommendationService.getRecommendation(recommendationPK);
        if (rec == null) {
            return ResponseEntity.ok(0);
        } else {
            return ResponseEntity.ok(rec.getValue());
        }
    }

    @PostMapping("/delete")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> deleteRecommendation(
            @RequestParam UUID eventId
    ) {
        User user = CESUtils.getAuthorizedUser();
        recommendationService.deleteRecommendation(user.getId(), eventId);
        return ResponseEntity.ok("Recommendation deleted");
    }

    @PostMapping("/get-ratings")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getRatings() {
        User user = CESUtils.getAuthorizedUser();
        List<Recommendation> recommendations = recommendationService.getRecommendationsByUserId(user.getId());
        return ResponseEntity.ok(recommendations.stream().map((r) -> r.getRecommendId().getEventId()).toList());
    }

//    @PostMapping("/update")
//    @PreAuthorize("isAuthenticated()")
//    public ResponseEntity<?> updateRecommendation(
//            @RequestParam UUID eventId,
//            @RequestParam Long rating
//    ) {
//        User user = CESUtils.getAuthorizedUser();
//        recommendationService.deleteRecommendation(user.getId(), eventId);
//        return ResponseEntity.ok(recommendationService.addRecommendation(user.getId(), eventId, rating));
//    }

    @PostMapping("/get-recommendation")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getRecommendation() {
        User user = CESUtils.getAuthorizedUser();
        UUID userId = user.getId();
        Map<UUID, Long> eventRating = recommendationService.getEventRating();
        List<UUID> eventIds = eventRating.keySet().stream().toList();
        Map<UUID, Integer> eventIndex = new HashMap<>();
        for (int i = 0; i < eventIds.size(); i++) {
            eventIndex.put(eventIds.get(i), i);
        }
        if (eventIds.isEmpty()) {
            return ResponseEntity.ok(new ArrayList<>());
        }
        double[][] similarityMatrix = new double[eventIds.size()][eventIds.size()];
        for (int i = 0; i < eventIds.size(); i++) {
            for (int j = 0; j <= i; j++) {
                if (i == j) {
                    similarityMatrix[i][j] = 1;
                } else {
                    Long intersection = recommendationService.getIntersectCount(
                            eventIds.get(i),
                            eventIds.get(j)
                    );
                    similarityMatrix[i][j] = intersection / Math.sqrt(
                            eventRating.get(eventIds.get(i)) * eventRating.get(eventIds.get(j))
                    );
                    similarityMatrix[j][i] = similarityMatrix[i][j];
                }
            }
        }
        List<Recommendation> recommendations =
                recommendationService.getRecommendationsByUserId(userId);
        List<UUID> ratedEventIds = recommendations.stream().map(
                recommendation -> recommendation.getRecommendId().getEventId()
        ).toList();
        List<Integer> ratedEventIndex = ratedEventIds.stream().map(eventIndex::get).toList();
        Map<UUID, Double> recommendationMap = new HashMap<>();
        for (int i = 0; i < eventIds.size(); i++) {
            UUID eventUUID = eventIds.get(i);
            double rating = 0;
            for (int j = 0; j < ratedEventIndex.size(); j++) {
                int ratedIndex = ratedEventIndex.get(j);
                rating += similarityMatrix[i][ratedIndex] * recommendations.get(j).getValue();
            }
            recommendationMap.put(eventUUID, rating);
        }
        return ResponseEntity.ok(recommendationMap);
    }
}
