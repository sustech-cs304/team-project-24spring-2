package cn.edu.sustech.ces.entity;


import cn.edu.sustech.ces.entity.Order;
import cn.edu.sustech.ces.entity.Event;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

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
    private String realname;//真实姓名
    private String description;
    private String email;
    private String password;
    private String phone;
    private int Gender;//0为男，1为女
    private int authority;//权限,0为普通用户，1为活动管理员（有权利创建活动），2为超级管理员
    private String avatar;//头像(图片链接)



    public User(String name, String email) {
        this.nickname = name;
        this.email = email;
    }

}
