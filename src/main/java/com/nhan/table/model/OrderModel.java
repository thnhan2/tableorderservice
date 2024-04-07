package com.nhan.table.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.nhan.table.common.OrderStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "orders")
public class OrderModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "table_id")
    private Long tableId;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private OrderStatus orderStatus;

    @Column(name = "created_at")
    private Date createdAt;
    @Column(name = "purchase_time")
    private Date purchaseTime;

    private double totalPrice;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<OrderItemModel> orderItems;

    public OrderModel(Long tableId, String status, Date createdAt, Date purchaseTime, double totalPrice) {
        this.tableId = tableId;
        this.orderStatus = OrderStatus.valueOf(status);
        this.createdAt = createdAt;
        this.purchaseTime =  purchaseTime;
        this.totalPrice = totalPrice;

    }

    public void addItem(OrderItemModel orderItemModel) {
        this.orderItems.add(orderItemModel);
    }

    public void totalPrice() {
    }
}
