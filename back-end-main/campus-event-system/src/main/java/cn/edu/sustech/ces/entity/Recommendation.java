package cn.edu.sustech.ces.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import lombok.*;




@Entity(name = "recommendations")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class Recommendation {
    @EmbeddedId
    private RecommendationPK recommend_id;
    private Long value;
}
