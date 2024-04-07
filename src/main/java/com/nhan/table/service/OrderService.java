package com.nhan.table.service;

import com.nhan.table.model.OrderItemModel;
import com.nhan.table.model.OrderModel;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface OrderService {
    List<OrderItemModel> getAllOrders();
    OrderModel createOrder(OrderModel order);
    OrderModel getOrderById(Long orderId);
    void deleteOrder(Long orderId);
    OrderModel updateOrder(Long orderId, OrderModel updatedOrder);

    void updateAmount(Long orderId);
    OrderItemModel getItemById(Long itemId);
    ResponseEntity<Object> addOrderItem(Long orderId, OrderItemModel orderItemModel);
}