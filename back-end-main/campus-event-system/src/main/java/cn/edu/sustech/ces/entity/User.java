package cn.edu.sustech.ces.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "user")
@Data
@NoArgsConstructor
@AllArgsConstructor

public class User {

    @Id
    private Integer id;
    private String username;
    private String email;

}
