package cn.edu.sustech.ces.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Entity(name = "user_tickets")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class UserTicket {

    @Id
    @UuidGenerator
    private UUID id;

    @Column(nullable = false)
    private UUID ticketId;

    @Column(nullable = false)
    private Integer number;

}
