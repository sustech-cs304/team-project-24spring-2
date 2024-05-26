package cn.edu.sustech.ces.service;

import cn.edu.sustech.ces.entity.Recommendation;
import cn.edu.sustech.ces.entity.RecommendationPK;
import cn.edu.sustech.ces.repository.RecommendationRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@AllArgsConstructor
public class RecommendationService {
    private final RecommendationRepository recommendationRepository;

    public Recommendation addRecommendation(UUID userId, UUID eventId, Long rating) {
        RecommendationPK recommendationPK = new RecommendationPK(userId, eventId);
        Recommendation recommendation = new Recommendation(recommendationPK, rating);
        return addRecommendation(recommendation);
    }

    public Recommendation addRecommendation(Recommendation recommendation) {
        recommendationRepository.save(recommendation);
        return recommendation;
    }
    public void deleteRecommendation(UUID userId, UUID eventId) {
        RecommendationPK pk = new RecommendationPK(userId, eventId);
        recommendationRepository.deleteById(pk);
    }

    public Recommendation getRecommendation(RecommendationPK recommendationPK) {
        return recommendationRepository.findById(recommendationPK).orElse(null);
    }

    public Map<UUID, Long> getEventRating() {
        Map<UUID, Long> eventRating = new java.util.HashMap<>();
        List<Object[]> eventUserCounts = recommendationRepository.getEventUserCounts();
        for (Object[] eventUserCount : eventUserCounts) {
            eventRating.put((UUID) eventUserCount[0], (Long) eventUserCount[1]);
        }
        return eventRating;
    }

    public Long getIntersectCount(UUID event1, UUID event2) {
        return recommendationRepository.getIntersectCount(event1, event2);
    }

    public List<Recommendation> getRecommendationsByUserId(UUID userId) {
        return recommendationRepository.findAllByRecommendIdUserId(userId);
    }
}
