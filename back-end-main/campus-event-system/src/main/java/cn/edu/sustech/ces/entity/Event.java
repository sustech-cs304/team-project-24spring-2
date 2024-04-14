package cn.edu.sustech.ces.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.time.ZonedDateTime;
import java.util.UUID;

@Entity(name = "events")
@NoArgsConstructor
@Data
@Setter
@Getter
public class Event {
    @Id
    @UuidGenerator
    private UUID id;
    private String title;
    private UUID publisher;
    private ZonedDateTime publishTime, startTime, endTime;
    private String description;
    private UUID location;
    private Integer availableCapacity;
    private Integer currentCapacity;

    public Event(String title, UUID publisher, ZonedDateTime publishTime, ZonedDateTime startTime, ZonedDateTime endTime, String description, UUID location, Integer availableCapacity, Integer currentCapacity) {
        this.title = title;
        this.publisher = publisher;
        this.publishTime = publishTime;
        this.startTime = startTime;
        this.endTime = endTime;
        this.description = description;
        this.location = location;
        this.availableCapacity = availableCapacity;
        this.currentCapacity = currentCapacity;
    }
}
