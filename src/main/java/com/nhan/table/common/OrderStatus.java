package com.nhan.table.common;

import lombok.Getter;

@Getter
public enum OrderStatus {
    ORDERING("order item"),
    PAID("complete payment");

    private final String status;

    OrderStatus(String status) {
        this.status = status;
    }

}
