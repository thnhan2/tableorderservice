package com.nhan.table.repository;

import com.nhan.table.model.TableModel;
import jakarta.persistence.Table;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TableRepository extends JpaRepository<TableModel, Long> {
}
