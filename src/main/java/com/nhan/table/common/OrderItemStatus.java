package com.nhan.table.common;

public enum OrderItemStatus {
    ORDERED("item hava been ordered"),
    COOKING("item being cooked"),
    READY("item is ready to serve"),
    CANCELLED("item has been cancelled"),
    RETURNED("item has been returned");

    private final String status;

    OrderItemStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return status;
    }

    public static OrderItemStatus fromString(String value) {
        for (OrderItemStatus status : OrderItemStatus.values()) {
            if (status.status.equalsIgnoreCase(value)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Invalid OrderItemStatus: " + value);
    }
}