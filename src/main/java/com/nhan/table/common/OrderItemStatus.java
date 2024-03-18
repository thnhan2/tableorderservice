package com.nhan.table.common;

public enum OrderItemStatus {
    PENDING("Pending"),
    IN_PROGRESS("In progress"),
    READY("Ready"),
    DELIVERED("Delivered"),
    COMPLETED("Completed"),
    CANCELLED("Cancelled"),
    REFUNDED("Refunded"),
    ERROR("Error"),
    RETURNED("Returned");

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