package com.nhan.table.repository;

import com.nhan.table.model.OrderItemModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItemModel, Long> {
}
