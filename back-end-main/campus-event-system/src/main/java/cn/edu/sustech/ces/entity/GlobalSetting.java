package cn.edu.sustech.ces.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

@Entity(name = "settings")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Setter
@Getter
public class GlobalSetting {

    @Id
    private String key;
    private String value;

}
