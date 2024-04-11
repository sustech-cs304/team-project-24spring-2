package cn.edu.sustech.ces.entity;

import lombok.Getter;

@Getter
public enum OrderStatus {

    UNPAID(0),
    PAID(1),
    CANCELED(2);

    private final int value;

    OrderStatus(int value) {
        this.value = value;
    }

}
