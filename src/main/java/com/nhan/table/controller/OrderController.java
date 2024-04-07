package com.nhan.table.controller;

import com.nhan.table.common.OrderStatus;
import com.nhan.table.common.util.TimeZoneHelper;
import com.nhan.table.model.OrderItemModel;
import com.nhan.table.model.OrderModel;
import com.nhan.table.service.TableService;
import com.nhan.table.service.implement.OrderServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderServiceImpl orderService;
    @Autowired
    private final RestTemplate restTemplate;
    @Value("${item.api}")
    private String itemAPI;

    public OrderController(OrderServiceImpl orderService, RestTemplate restTemplate) {
        this.orderService = orderService;
        this.restTemplate = restTemplate;
    }

    @Operation(description = "Get All Order data")
    @GetMapping
    public List<HashMap<String, String>> getAllOrders() {
            List<OrderItemModel> orders = orderService.getAllOrders();
            List<HashMap<String, String>> ordersAsMap = new ArrayList<>();

            for (OrderItemModel order : orders) {
                HashMap<String, String> orderMap = new HashMap<>();
                orderMap.put("name", order.getName());
                orderMap.put("tableId", String.valueOf(orderService.getOrderById(order.getOrder().getId()).getTableId()));
                orderMap.put("orderId", orderService.getOrderById(order.getOrder().getId()).getId().toString());
                orderMap.put("quantity", String.valueOf(order.getQty()));
                orderMap.put("note", order.getNote());
                orderMap.put("status", order.getItemStatus());
                orderMap.put("orderItemId", order.getId().toString());
                orderMap.put("orderTime", String.valueOf(order.getOrderTime()).substring(11,19));
                ordersAsMap.add(orderMap);
            }

            return ordersAsMap;
    }

    @Operation(description = "Get Order Info By orderId")
    @GetMapping("/{orderId}")
    public ResponseEntity<OrderModel> getOrderById(@PathVariable Long orderId) {
        OrderModel order = orderService.getOrderById(orderId);
        return ResponseEntity.ok(order);
    }

    @Operation(description = "Create new order/open a table")
    @PostMapping
    public ResponseEntity<?> createOrder(@RequestBody OrderModel order) {
        OrderModel createdOrder = orderService.createOrder(order);
        if (createdOrder == null) {
            return new ResponseEntity<>("table is Not Available", HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(createdOrder);
    }

    @Operation(description = "Update Order Info")
    @PutMapping("/{orderId}")
    public ResponseEntity<OrderModel> updateOrder(@PathVariable Long orderId, @RequestBody OrderModel updatedOrder) {
        OrderModel updated = orderService.updateOrder(orderId, updatedOrder);
        return ResponseEntity.ok(updated);
    }

    @Operation(description = "Cancel orders/close table")
    @DeleteMapping("/{orderId}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long orderId) {
        orderService.deleteOrder(orderId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/orderItems")
    public List<OrderItemModel> getItems() {
        return orderService.getAllItems();
    }


    @Operation(description = "Add item to orders")
    @PutMapping("/addItem/{orderId}")
    public ResponseEntity<?> addOrderItem(@PathVariable Long orderId, @RequestBody OrderItemModel orderItem) {
        if (orderItem.getQty() <= 0) {
            return new ResponseEntity<>("quantity must be greater than 0", HttpStatus.BAD_REQUEST);
        }
        return orderService.addOrderItem(orderId, orderItem);
    }

    @PutMapping("/editItem")
    public ResponseEntity<?> editItemStatus(@RequestBody OrderItemModel request) {
        orderService.updateOderItem(request);
        return ResponseEntity.ok().build();
    }

    @Operation(description = "Get bill information")
    @GetMapping("/bill/{orderId}")
    public ResponseEntity<Map<String, Object>> getBill(@PathVariable long orderId) throws ExecutionException, InterruptedException {
        Map<String, Object> map = new HashMap<>();
        OrderModel order = orderService.getOrderById(orderId);
        if (order == null) {
            map.put("message", "Order id does not exist.");
            return new ResponseEntity<>(map, HttpStatus.NO_CONTENT);
        }

        List<Map<String, Object>> itemList = getItemList(order);
        List<Map<String, Object>> finalItemList = getFinalItemList(itemList);

        map.put("orderId", order.getId());
        map.put("createTime", order.getCreatedAt());
        map.put("purchaseTime", order.getPurchaseTime());
        map.put("itemList", finalItemList);
        map.put("orderStatus", order.getOrderStatus());
        map.put("total", calculateTotalPrice(finalItemList) );

        return ResponseEntity.ok(map);
    }

    private List<Map<String, Object>> getItemList(OrderModel order) throws InterruptedException, ExecutionException {
        List<Map<String, Object>> itemList = new ArrayList<>();
        List<CompletableFuture<Void>> futures = new ArrayList<>();
        RestTemplate restTemplate = new RestTemplate();
        for (OrderItemModel i : order.getOrderItems()) {
            CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                Map<String, Object> itemMap = getItemMap(i, restTemplate);
                synchronized (itemList) {
                    itemList.add(itemMap);
                }
            });
            futures.add(future);
        }
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).get();
        return itemList;
    }

    private Map<String, Object> getItemMap(OrderItemModel i, RestTemplate restTemplate) {

        String itemPriceUrl = itemAPI + i.getItemId();
        ResponseEntity<Map> responseEntity = restTemplate.getForEntity(itemPriceUrl, Map.class);
        Map responseBody = responseEntity.getBody();
        double price = 0.0;
        String name = "";

        if (responseBody != null) {
            Object priceObject = responseBody.get("price");
            if (priceObject instanceof Number) {
                price = ((Number) priceObject).doubleValue();
            }
            Object nameObject = responseBody.get("name");
            if (nameObject instanceof String) {
                name = (String) nameObject;
            }
        }

        Map<String, Object> itemMap = new HashMap<>();
        itemMap.put("itemId", i.getItemId());
        itemMap.put("qty", i.getQty());
        itemMap.put("price", price);
        itemMap.put("name", name);
        return itemMap;
    }

    private List<Map<String, Object>> getFinalItemList(List<Map<String, Object>> itemList) {
        // Map to save item qty
        Map<String, Integer> itemQuantities = new HashMap<>();
        // Map to save name of items
        Map<String, String> itemNames = new HashMap<>();

        // Final list with group same item
        List<Map<String, Object>> finalItemList = new ArrayList<>();
        for (Map<String, Object> item : itemList) {
            String itemId = (String) item.get("itemId");
            int quantity = itemQuantities.getOrDefault(itemId, 0) + (int) item.get("qty");
            itemQuantities.put(itemId, quantity);

            String name = (String) item.get("name");
            itemNames.put(itemId, name);
        }

        for (Map.Entry<String, Integer> entry : itemQuantities.entrySet()) {
            String itemId = entry.getKey();
            int quantity = entry.getValue();
            double price = itemList.stream()
                    .filter(item -> item.get("itemId").equals(itemId))
                    .mapToDouble(item -> (double) item.get("price"))
                    .sum();
            String name = itemNames.get(itemId);

            Map<String, Object> finalItem = new HashMap<>();
            finalItem.put("itemId", itemId);
            finalItem.put("qty", quantity);
            finalItem.put("price", price);
            finalItem.put("name", name);
            finalItemList.add(finalItem);
        }

        return finalItemList;
    }

    private double calculateTotalPrice(List<Map<String, Object>> finalItemList) {
        double totalPrice = 0.0;
        for (Map<String, Object> item : finalItemList) {
            double price = (double) item.get("price");
            totalPrice += price;
        }
        return totalPrice;
    }

}