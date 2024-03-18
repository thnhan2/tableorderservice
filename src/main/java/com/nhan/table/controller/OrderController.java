package com.nhan.table.controller;

import com.nhan.table.model.OrderItemModel;
import com.nhan.table.model.OrderModel;
import com.nhan.table.service.TableService;
import com.nhan.table.service.implement.OrderServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderServiceImpl orderService;

    public OrderController(OrderServiceImpl orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public List<OrderModel> getAllOrders() {
        return orderService.getAllOrders();
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderModel> getOrderById(@PathVariable Long orderId) {
        OrderModel order = orderService.getOrderById(orderId);
        return ResponseEntity.ok(order);
    }

    @Operation(description = "Create new order/open a table")
    @PostMapping
    public ResponseEntity<OrderModel> createOrder(@RequestBody OrderModel order) {
        OrderModel createdOrder = orderService.createOrder(order);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdOrder);
    }

    @PutMapping("/{orderId}")
    public ResponseEntity<OrderModel> updateOrder(@PathVariable Long orderId, @RequestBody OrderModel updatedOrder) {
        OrderModel updated = orderService.updateOrder(orderId, updatedOrder);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{orderId}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long orderId) {
        orderService.deleteOrder(orderId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{orderId}")
    public ResponseEntity<?> addOrderItem(@PathVariable Long orderId, @RequestBody OrderItemModel orderItem) {
        return orderService.addOrderItem(orderId, orderItem);
    }

}