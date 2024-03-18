package com.nhan.table.repository;

import com.nhan.table.model.OrderItemModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItemModel, String> {

}
