package cn.edu.sustech.ces.entity;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.UUID;
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Embeddable
public class RecommendationPK implements Serializable {
    private UUID user_id;
    private UUID event_id;
}
