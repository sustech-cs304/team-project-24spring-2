package cn.edu.sustech.ces.entity;



import cn.edu.sustech.ces.enums.PermissionGroup;
import cn.edu.sustech.ces.enums.UserGender;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.util.List;
import java.util.UUID;

@Entity(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class User {
    @Id
    @UuidGenerator
    private UUID id;

    private String nickname;

    private String realName;

    private String description;

    private String email;

    private String password;

    private String phone;

    @Enumerated(EnumType.ORDINAL)
    private UserGender gender;

    @Enumerated(EnumType.ORDINAL)
    private PermissionGroup permissionGroup;

    private String avatarUrl;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_events",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "event_id"))
    private List<Event> events;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "payerId")
    private List<Order> orders;


}
