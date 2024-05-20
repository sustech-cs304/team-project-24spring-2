package cn.edu.sustech.ces.service;

import cn.edu.sustech.ces.entity.Recommendation;
import cn.edu.sustech.ces.repository.RecommendationRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class RecommendationService {
    private final RecommendationRepository recommendationRepository;
    public Recommendation addRecommendation(Recommendation recommendation) {
        recommendationRepository.save(recommendation);
        return recommendation;
    }
}
