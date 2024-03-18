package com.nhan.table.controller;

import com.nhan.table.model.TableModel;
import com.nhan.table.service.TableService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/tables")
public class TableController {

    private final TableService tableService;

    public TableController(TableService tableService) {
        this.tableService = tableService;
    }

    @PostMapping("/add")
    public ResponseEntity<?> addTable(@RequestBody TableModel tableModel) {
        TableModel addedTableModel = tableService.addTable(tableModel);
        if (addedTableModel != null) {
            return ResponseEntity.ok(addedTableModel);
        } else {
            return ResponseEntity.badRequest().body("Invalid seat count");
        }
    }

    @PostMapping("/open/{tableId}")
    public ResponseEntity<?> openTable(@PathVariable Long tableId) {
        if (tableService.openTable(tableId)) {
            return ResponseEntity.ok("Table opened successfully");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Table not found");
        }
    }

    @PostMapping("/close/{tableId}")
    public ResponseEntity<?> closeTable(@PathVariable Long tableId) {
        if (tableService.closeTable(tableId)) {
            return ResponseEntity.ok("Table closed successfully");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Table not found");
        }
    }

    @GetMapping("/status/{tableId}")
    public ResponseEntity<?> getTableStatus(@PathVariable Long tableId) {
        String status = tableService.getTableStatus(tableId);
        if (status != null) {
            return ResponseEntity.ok(status);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Table not found");
        }
    }

    @GetMapping("/{tableId}")
    public ResponseEntity<?> getTableById(@PathVariable Long tableId) {
        TableModel tableModel = tableService.findById(tableId);
        if (tableModel != null) {
            return ResponseEntity.ok(tableModel);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Table not found");
        }
    }


    @GetMapping("/all")
    public ResponseEntity<List<TableModel>> getAllTables() {
        List<TableModel> tables = tableService.getAllTable();
        return ResponseEntity.ok(tables);
    }
}