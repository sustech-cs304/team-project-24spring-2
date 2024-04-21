package cn.edu.sustech.ces.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Entity(name = "tickets")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Setter
@Getter
public class Ticket {

    @Id
    @UuidGenerator
    private UUID id;

    @Column(nullable = false)
    private UUID eventId;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private Double price;

    @Column(nullable = false)
    private Integer totalAmount;

    @Column(nullable = false)
    private Integer soldAmount;

}
