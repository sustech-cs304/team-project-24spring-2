package cn.edu.sustech.ces.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Entity(name = "comments")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Setter
@Getter
public class Comment {

    @Id
    @UuidGenerator
    private UUID id;

    @Column(nullable = false)
    private UUID eventId;

    @Column(nullable = false)
    private UUID userId;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private Long createTime;

}
