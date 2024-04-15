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

    private String name;

    private String description;

    private Double price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payer_id", nullable = false)
    private User payer;

    @Enumerated(EnumType.ORDINAL)
    private OrderStatus status;

    @Enumerated(EnumType.ORDINAL)
    private PurchaseMethod purchaseMethod;

    private Long purchaseFinishTime;

    private Long purchaseCreateTime;

}
