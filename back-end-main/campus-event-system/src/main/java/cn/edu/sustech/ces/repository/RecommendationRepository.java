package cn.edu.sustech.ces.repository;

import cn.edu.sustech.ces.entity.Recommendation;
import cn.edu.sustech.ces.entity.RecommendationPK;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;
import cn.edu.sustech.ces.entity.Recommendation;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Repository
public interface RecommendationRepository extends ListCrudRepository<Recommendation, RecommendationPK> {
    @Query("SELECT r.recommend_id.event_id AS eventId, COUNT(r.recommend_id.user_id) AS userCount FROM recommendations r GROUP BY r.recommend_id.event_id")
    List<Object[]> getEventUserCounts();

    @Query("SELECT COUNT(DISTINCT r1.recommend_id.user_id) FROM recommendations r1, recommendations r2 WHERE r1.recommend_id.event_id = ?1 AND r2.recommend_id.event_id = ?2 AND r1.recommend_id.user_id = r2.recommend_id.user_id")
    Long getIntersectCount(UUID event1, UUID event2);

    @Query("SELECT r FROM recommendations r WHERE r.recommend_id.user_id = ?1")
    List<Recommendation> findAllByRecommend_id_User_id(UUID userID);
}
