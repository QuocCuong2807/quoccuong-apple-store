package com.springteam.backend.service;

import com.springteam.backend.dto.CartDto;
import com.springteam.backend.dto.DataChartDto;
import com.springteam.backend.dto.OrderDto;
import com.springteam.backend.dto.OrderResponse;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface IOrderService {
    void saveOrder(CartDto cart, String province, String district, String ward);
    OrderResponse getAllOrders(int p, String statusFilter);
    OrderDto getOrderById(Long id);

}
