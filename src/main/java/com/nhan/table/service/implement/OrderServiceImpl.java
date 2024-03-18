package com.nhan.table.service.implement;

import com.nhan.table.common.TableStatus;
import com.nhan.table.model.OrderItemModel;
import com.nhan.table.model.OrderModel;
import com.nhan.table.repository.OrderItemRepository;
import com.nhan.table.repository.OrderRepository;
import com.nhan.table.service.OrderService;
import com.nhan.table.service.TableService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private TableService tableService;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Override
    public List<OrderModel> getAllOrders() {
        return orderRepository.findAll();
    }

    @Override
    public OrderModel createOrder(OrderModel order) {
        if (tableService.getTableStatus(order.getTableId()).equals(String.valueOf(TableStatus.AVAILABLE))) {
            tableService.openTable(order.getTableId());
        }

        return orderRepository.save(order);
    }

    @Override
    public OrderModel getOrderById(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Order not found with id: " + orderId));
    }

    @Override
    public void deleteOrder(Long orderId) {
        orderRepository.deleteById(orderId);
    }

    @Override
    public OrderModel updateOrder(Long orderId, OrderModel updatedOrder) {
        OrderModel existingOrder = getOrderById(orderId);
        existingOrder.setCreatedAt(updatedOrder.getCreatedAt());
        existingOrder.setPurchaseTime(updatedOrder.getPurchaseTime());
        existingOrder.setOrderItems(updatedOrder.getOrderItems());
        return orderRepository.save(existingOrder);
    }


    @Override
    public ResponseEntity<String> addOrderItem(Long orderId, OrderItemModel orderItemModel) {
        Optional<OrderModel> orderOptional = orderRepository.findById(orderId);

        if (!orderOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Order not found");
        }

        OrderModel order = orderOptional.get();
        orderItemModel.setOrder(order);

        orderItemRepository.save(orderItemModel);

        return ResponseEntity.status(HttpStatus.CREATED).body("Order item added successfully");
    }
}