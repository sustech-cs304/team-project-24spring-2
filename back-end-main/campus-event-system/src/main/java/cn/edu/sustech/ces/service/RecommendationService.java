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
        return null;
    }
}
