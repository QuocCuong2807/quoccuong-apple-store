package com.springteam.backend.controller;

import com.springteam.backend.dto.DataChartDto;
import com.springteam.backend.dto.OrderDto;
import com.springteam.backend.dto.OrderResponse;
import com.springteam.backend.service.IOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/order")
public class OrderController {
    private IOrderService orderService;

    @Autowired
    public OrderController(IOrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/orders")
    public ResponseEntity<OrderResponse> getAllOrders(@RequestParam Optional<Integer> p, @RequestParam String statusFilter) {
        OrderResponse orderResponse = orderService.getAllOrders(p.orElse(0), statusFilter);
        return new ResponseEntity<>(orderResponse, HttpStatus.OK);
    }

    @GetMapping("/order-by-id")
    public ResponseEntity<OrderDto> getOrdersById(@RequestParam(name = "id") String productId) {
        long id = productId.isEmpty() ? 0 : Long.valueOf(productId);
        OrderDto orderDto = orderService.getOrderById(id);
        return new ResponseEntity<>(orderDto, HttpStatus.OK);
    }

}
