package com.springteam.backend.service;

import com.springteam.backend.dto.DataChartDto;
import com.springteam.backend.repository.IOrderRepository;
import com.springteam.backend.repository.IProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DashboardServiceImpl implements IDashboardService {

    private final String TOTAL_REVENUE = "TOTAL_REVENUE";
    private final String CURRENT_MONTH_REVENUE = "CRR_MONTH";
    private final String CURRENT_WEEK_REVENUE = "CRR_WEEK";
    private final String LAST_WEEK_REVENUE = "LAST_WEEK";

    private IOrderRepository orderRepository;
    private IProductRepository productRepository;

    @Autowired
    public DashboardServiceImpl(IOrderRepository orderRepository, IProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
    }

    @Override
    public Long getOrderQuantity() {
        return orderRepository.count();
    }

    @Override
    public Long getTotalRevenue() {
        return orderRepository.getTotalRevenue();
    }

    @Override
    public List<DataChartDto> getRevenueDataChartByCategory(String filter) {
        List<DataChartDto> data = new ArrayList<>();
        List<DataChartDto> emptyData = new ArrayList<>();
        emptyData.add(DataChartDto.builder().name("iPhone").data(0L).build());
        emptyData.add(DataChartDto.builder().name("iPad").data(0L).build());
        emptyData.add(DataChartDto.builder().name("Mac").data(0L).build());
        switch (filter) {
            case TOTAL_REVENUE:
                data = orderRepository.getTotalRevenueByCategory();
                break;
            case CURRENT_MONTH_REVENUE:
                data = orderRepository.getRevenueByCategoryForCurrentMonth();
                break;
            case CURRENT_WEEK_REVENUE:
                data = orderRepository.getRevenueByCategoryForCurrentWeek();
                break;
            case LAST_WEEK_REVENUE:
                data = orderRepository.getRevenueByCategoryForLastWeek();
                break;
        }

        return data.isEmpty() ? emptyData : data;
    }

    @Override
    public long getProductQuantity() {
        return productRepository.count();
    }
}
