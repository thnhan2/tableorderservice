package com.nhan.table.common;

import lombok.Getter;

/*
* AVAILABLE:
* OCCUPIED: in use
* UNAVAILABLE:
* */
@Getter
public enum TableStatus {
    AVAILABLE("Available"),
    OCCUPIED("Occupied"),
    UNAVAILABLE("Unavailable");

    private final String status;

    TableStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return status;
    }
}