package cn.edu.sustech.ces.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.time.ZonedDateTime;
import java.util.Set;
import java.util.UUID;

@Entity(name = "events")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Setter
@Getter
public class Event {
    @Id
    @UuidGenerator
    private UUID id;

    private String title;

    private UUID publisher;

    private Long publishTime;

    private Long startTime;

    private Long endTime;

    private String description;

    private Integer availableCapacity;

    private Integer altitude;

    private Integer longitude;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_events",
            joinColumns = @JoinColumn(name = "event_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private Set<User> participants;

    public void setLocation(int altitude, int longitude) {
        this.altitude = altitude;
        this.longitude = longitude;
    }

}
