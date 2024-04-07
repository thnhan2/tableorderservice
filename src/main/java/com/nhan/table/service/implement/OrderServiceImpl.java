package com.nhan.table.service.implement;

import com.nhan.table.common.OrderStatus;
import com.nhan.table.common.TableStatus;
import com.nhan.table.common.util.TimeZoneHelper;
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

import java.util.Date;
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
    public List<OrderItemModel> getAllOrders() {
        return orderItemRepository.findAll();
    }

    @Override
    public OrderModel createOrder(OrderModel order) {
        if (tableService.getTableStatus(order.getTableId()).equals(String.valueOf(TableStatus.AVAILABLE))) {
            tableService.openTable(order.getTableId());
        }
        else  {
            return null;
        }
        order.setCreatedAt(TimeZoneHelper.convertToTimeZone(new Date()));
        order.setPurchaseTime(null);
        order.setOrderStatus(OrderStatus.valueOf("ORDERING"));
        return orderRepository.save(order);
    }

    @Override
    public OrderModel getOrderById(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Order not found with id: " + orderId));
    }

    public void updateOderItem(OrderItemModel orderItemModel) {
        System.out.println("xin chao" + orderItemModel);
        OrderItemModel orderItem = orderItemRepository.findById(orderItemModel.getId())
                .orElseThrow(() -> new EntityNotFoundException("OrderItem not found with id: " + orderItemModel.getItemId()));
        orderItem.setItemStatus(orderItemModel.getItemStatus());
        orderItemRepository.save(orderItem);
    }

    @Override
    public void deleteOrder(Long orderId) {
        orderRepository.deleteById(orderId);
    }

    @Override
    public OrderModel updateOrder(Long orderId, OrderModel updatedOrder) {
        OrderModel existingOrder = getOrderById(orderId);
        if (updatedOrder.getOrderStatus() != null)
            existingOrder.setCreatedAt(updatedOrder.getCreatedAt());
        if (updatedOrder.getOrderItems()!= null)
            existingOrder.setOrderItems(updatedOrder.getOrderItems());
        if(updatedOrder.getOrderStatus()!= null)
            existingOrder.setOrderStatus(updatedOrder.getOrderStatus());
        return orderRepository.save(existingOrder);
    }

    @Override
    public void updateAmount(Long orderId) {
        if (orderRepository.findById(orderId).isPresent()) {
            OrderModel orderModel = orderRepository.findById(orderId).get();
            orderModel.totalPrice();
        }
    }

    @Override
    public OrderItemModel getItemById(Long itemId) {
        Optional<OrderItemModel> itemModels = orderItemRepository.findById(itemId);
        return itemModels.orElse(null);
    }

    public List<OrderItemModel> getAllItems() {
        return orderItemRepository.findAll();
    }


    @Override
    public ResponseEntity<Object> addOrderItem(Long orderId, OrderItemModel orderItemModel) {
        Optional<OrderModel> orderOptional = orderRepository.findById(orderId);

        if (orderOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        OrderModel order = orderOptional.get();

        OrderItemModel item = new OrderItemModel();
        item.setItemId(orderItemModel.getItemId());
        item.setItemStatus("ORDERED");
        item.setQty(orderItemModel.getQty());
        item.setNote(orderItemModel.getNote());
        item.setName(orderItemModel.getName());
        item.setOrderTime(TimeZoneHelper.convertToTimeZone(new Date()));
        item.setOrder(order);

        OrderItemModel savedItem = orderItemRepository.save(item);
        order.totalPrice();
        orderRepository.save(order);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedItem);
    }

}