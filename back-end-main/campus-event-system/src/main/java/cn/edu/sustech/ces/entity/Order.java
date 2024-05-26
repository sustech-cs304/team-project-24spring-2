package cn.edu.sustech.ces.entity;


import cn.edu.sustech.ces.enums.OrderStatus;
import cn.edu.sustech.ces.enums.PurchaseMethod;

import jakarta.persistence.*;
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

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private UUID ticketId;

    @Column(nullable = false)
    private Double price;

    @Column(nullable = false)
    private UUID payerId;

    @Enumerated(EnumType.ORDINAL)
    @Column(nullable = false)
    private OrderStatus status;

    @Enumerated(EnumType.ORDINAL)
    private PurchaseMethod purchaseMethod;

    private Long purchaseFinishTime;

    private Long purchaseCreateTime;

    @Column(nullable = false)
    private Long orderCreateTime;

}
