package com.nhan.table.service.implement;

import com.nhan.table.common.TableStatus;
import com.nhan.table.model.TableModel;
import com.nhan.table.repository.TableRepository;
import com.nhan.table.service.TableService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TableServiceImpl implements TableService {

    private final TableRepository tableRepository;

    public TableServiceImpl(TableRepository repository) {
        this.tableRepository = repository;
    }

    @Override
    public TableModel addTable(TableModel table) {
        if (table.getSeatCount() < 1) {
            return null;
        }
        return tableRepository.save(table);
    }

    @Override
    public boolean openTable(Long tableId) {
        TableModel tableModel = findById(tableId);
        if (tableModel != null) {
            if (tableModel.getTableStatus() == TableStatus.AVAILABLE) {
            tableModel.setTableStatus(TableStatus.OCCUPIED);
            tableRepository.save(tableModel);
            return true;
            }
        }
        return false;
    }

    @Override
    public boolean closeTable(Long tableId) {
        TableModel tableModel = findById(tableId);
        if (tableModel != null) {
            tableModel.setTableStatus(TableStatus.AVAILABLE);
            tableRepository.save(tableModel);
            return true;
        }
        return false;
    }

    @Override
    public String getTableStatus(Long tableId) {
        TableModel tableModel = findById(tableId);
        if (tableModel != null) {
            return String.valueOf(tableModel.getTableStatus());
        }
        return null;
    }

    @Override
    public List<TableModel> getAllTable() {
        return tableRepository.findAll();
    }

    @Override
    public TableModel findById(long tableId) {
        Optional<TableModel> tableModel = tableRepository.findById(tableId);
        return tableModel.orElse(null);
    }
}
