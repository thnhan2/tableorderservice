package com.nhan.table.model;

import com.nhan.table.common.TableStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tables")
public class TableModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "seat_count")
    private int seatCount;

    private String location;

    @Enumerated(EnumType.STRING)
    private TableStatus tableStatus;

    public TableModel(int seatCount, String location, String status) {
        this.seatCount = seatCount;
        this.location = location;
        this.tableStatus = TableStatus.valueOf(status);
    }


    public TableModel(long id, int seatCount, String location, String s) {
        this.id = id;
        this.seatCount = seatCount;
        this.location = location;
        this.tableStatus = TableStatus.valueOf(s);
    }
}
