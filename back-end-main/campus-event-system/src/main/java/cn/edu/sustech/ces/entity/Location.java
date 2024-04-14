package cn.edu.sustech.ces.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Entity(name = "locations")
@Data
@NoArgsConstructor
@Setter
@Getter
public class Location {
    @Id
    @UuidGenerator
    private UUID id;
    private String name;
    private Double longitude, latitude;

    public Location(String name, Double latitude, Double longitude) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
