package com.springteam.backend.controller;

import com.springteam.backend.dto.DataChartDto;
import com.springteam.backend.service.IDashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/dashboard")
public class DashboardController {
    private IDashboardService dashboardService;

    @Autowired
    public DashboardController(IDashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }
    @GetMapping("/order-quantity")
    public ResponseEntity<Long> getQuantity() {
        long quantity = dashboardService.getOrderQuantity();
        return new ResponseEntity<>(quantity, HttpStatus.OK);
    }
    @GetMapping("/product-quantity")
    public ResponseEntity<Long> getProductQuantity() {
        Long quantity = dashboardService.getProductQuantity();
        return new ResponseEntity<>(quantity, HttpStatus.OK);
    }

    @GetMapping("/revenue")
    public ResponseEntity<Long> getTotalRevenue() {
        Long totalRevenue = dashboardService.getTotalRevenue();
        return new ResponseEntity<>(totalRevenue, HttpStatus.OK);
    }

    @GetMapping("/total-incomes-analysis")
    public ResponseEntity<List<DataChartDto>> getTotalIncomeAnalysis(@RequestParam String filter){
        List<DataChartDto> data = dashboardService.getRevenueDataChartByCategory(filter);
        return new ResponseEntity<>(data, HttpStatus.OK);
    }
}
