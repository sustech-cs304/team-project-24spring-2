package cn.edu.sustech.ces.entity;


import cn.edu.sustech.ces.enums.OrderStatus;
import cn.edu.sustech.ces.enums.PurchaseMethod;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;


@Entity(name = "orders")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class Order {

    @Id
    @UuidGenerator
    private UUID id;

    private String name;

    private String description;

    private double price;

    private UUID payerId;

    @Enumerated(EnumType.ORDINAL)
    private OrderStatus status;

    @Enumerated(EnumType.ORDINAL)
    private PurchaseMethod purchaseMethod;

    private long purchaseFinishTime;

    private long purchaseCreateTime;

}
