package cn.edu.sustech.ces.utils;
import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;
import java.util.UUID;

@Setter
@Getter
public class EventDTO {
    private String title;
    private UUID publisherId;
    private ZonedDateTime publishTime;
    private ZonedDateTime startTime;
    private ZonedDateTime endTime;
    private String description;
    private UUID locationId;
    private Integer availableCapacity;
    private Integer currentCapacity;
}