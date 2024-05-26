package cn.edu.sustech.ces.entity;

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
    private RecommendationPK recommendId;
    private Long value;
}
