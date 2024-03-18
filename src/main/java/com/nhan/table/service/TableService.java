package com.nhan.table.service;

import com.nhan.table.model.TableModel;

import java.util.List;

public interface TableService {

    TableModel addTable(TableModel table);
    boolean openTable(Long tableId);
    boolean closeTable(Long tableId);
    String getTableStatus(Long tableId);
    List<TableModel> getAllTable();
    TableModel findById(long tableId);
}
