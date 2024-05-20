package cn.edu.sustech.ces.repository;

import cn.edu.sustech.ces.entity.Recommendation;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface RecommendationRepository extends ListCrudRepository<Recommendation, UUID> {

}
