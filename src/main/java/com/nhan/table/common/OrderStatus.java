package com.nhan.table.common;

public enum OrderStatus {
    PENDING("Pending"),
    IN_PROGRESS("In progress"),
    READY("Ready"),
    ON_THE_WAY("On the way"),
    DELIVERED("Delivered"),
    COMPLETED("Completed"),
    CANCELLED("Cancelled"),
    REFUNDED("Refunded"),
    ERROR("Error"),
    RETURNED("Returned");

    private final String status;

    OrderStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
