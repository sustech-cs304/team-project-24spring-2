package cn.edu.sustech.ces.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.*;

import java.util.UUID;

@Entity(name = "user_event")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserEvent {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private UUID userId;

    @Column(nullable = false)
    private UUID eventId;

}
