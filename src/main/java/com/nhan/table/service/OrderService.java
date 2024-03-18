package com.nhan.table.service;

import com.nhan.table.model.OrderItemModel;
import com.nhan.table.model.OrderModel;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public interface OrderService {
    List<OrderModel> getAllOrders();
    OrderModel createOrder(OrderModel order);
    OrderModel getOrderById(Long orderId);
    void deleteOrder(Long orderId);
    OrderModel updateOrder(Long orderId, OrderModel updatedOrder);


    ResponseEntity<String> addOrderItem(Long orderId, OrderItemModel orderItemModel);
}