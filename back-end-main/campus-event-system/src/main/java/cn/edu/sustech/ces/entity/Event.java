package cn.edu.sustech.ces.entity;

import cn.edu.sustech.ces.enums.EventStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.time.ZonedDateTime;
import java.util.List;
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

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private UUID publisher;

    private Long publishTime;

    @Enumerated(EnumType.ORDINAL)
    private EventStatus status;

    @Column(nullable = false)
    private Long startTime;

    @Column(nullable = false)
    private Long endTime;

    @Column(nullable = false)
    private String documentUrl;

    private String imageUrl;

    @ElementCollection(fetch = FetchType.EAGER)
    private List<UUID> tickets;

    private Double latitude;

    private Double longitude;

    private Integer categoryId;

    private String locationName;

    public void setLocation(Double latitude, Double longitude, String locationName) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.locationName = locationName;
    }

}
