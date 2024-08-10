package com.springteam.backend.service;

import com.springteam.backend.dto.DataChartDto;

import java.util.List;

public interface IDashboardService {
    Long getOrderQuantity();
    Long getTotalRevenue();
    long getProductQuantity();

    List<DataChartDto> getRevenueDataChartByCategory(String filter);

}
