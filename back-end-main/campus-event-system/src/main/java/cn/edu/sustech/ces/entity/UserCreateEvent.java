package cn.edu.sustech.ces.entity;


import cn.edu.sustech.ces.entity.Order;
import cn.edu.sustech.ces.entity.Event;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;


@Entity(name = "user_create_event")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class UserCreateEvent {
    @Id
    @UuidGenerator
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "user_id")  // 指定数据库中的外键列名
    private User user;

    @ManyToOne
    @JoinColumn(name = "event_id")  // 假设数据库中的列名为 event_id
    private Event event;
}
