package cn.edu.sustech.ces.repository;

import cn.edu.sustech.ces.entity.Recommendation;
import cn.edu.sustech.ces.entity.RecommendationPK;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface RecommendationRepository extends ListCrudRepository<Recommendation, RecommendationPK> {
    @Query("SELECT r.recommendId.eventId AS eventId, COUNT(r.recommendId.userId) AS userCount FROM recommendations r GROUP BY r.recommendId.eventId")
    List<Object[]> getEventUserCounts();

    @Query("SELECT COUNT(DISTINCT r1.recommendId.userId) FROM recommendations r1, recommendations r2 WHERE r1.recommendId.eventId = ?1 AND r2.recommendId.eventId = ?2 AND r1.recommendId.userId = r2.recommendId.userId")
    Long getIntersectCount(UUID event1, UUID event2);

    @Query("SELECT r FROM recommendations r WHERE r.recommendId.userId = ?1")
    List<Recommendation> findAllByRecommendIdUserId(UUID userID);
}
