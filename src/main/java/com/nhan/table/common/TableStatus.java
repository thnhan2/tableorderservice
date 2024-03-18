package com.nhan.table.common;

/*
* AVAILABLE:
* OCCUPIED: in use
* UNAVAILABLE:
* pending: in processing
* */
public enum TableStatus {
    AVAILABLE("Available"),
    OCCUPIED("Occupied"),
    UNAVAILABLE("Unavailable"),
    PENDING("Pending");

    private final String status;

    TableStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return status;
    }
}