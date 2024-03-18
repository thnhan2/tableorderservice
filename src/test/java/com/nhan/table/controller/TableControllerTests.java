package com.nhan.table.controller;

import com.nhan.table.model.TableModel;
import com.nhan.table.service.implement.TableServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class TableControllerTests {

    private MockMvc mockMvc;

    @Mock
    private TableServiceImpl tableService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        TableController tableController = new TableController(tableService);
        mockMvc = MockMvcBuilders.standaloneSetup(tableController).build();
    }

    @Test
    public void testAddTable_Success() throws Exception {
        TableModel table = new TableModel();
        table.setId(1L);
        table.setLocation("Table 1");

        when(tableService.addTable(any(TableModel.class)))
                .thenReturn(table);

        mockMvc.perform(post("/api/tables")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"Table 1\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("Create Table Successfully"));
    }

    @Test
    public void testAddTable_BadRequest() throws Exception {
        when(tableService.addTable(any(TableModel.class)))
                .thenReturn(null);

        mockMvc.perform(post("/api/tables")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"Table 1\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Bad Request!"));
    }

    @Test
    public void testGetListTable() throws Exception {
        List<TableModel> tables = new ArrayList<>();
        tables.add(new TableModel(1L,1, "Table 1",null));

        when(tableService.getAllTable())
                .thenReturn(tables);

        mockMvc.perform(get("/api/tables"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].location").value("Table 1"));
    }

    @Test
    public void testGetTableById() throws Exception {
        TableModel table = new TableModel(1L,1,"Table 1",null);

        when(tableService.findById(1L))
                .thenReturn(table);

        mockMvc.perform(get("/api/tables/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.location").value("Table 1"));
    }
}