package com.springteam.backend.service;

import com.springteam.backend.dto.DataChartDto;
import com.springteam.backend.entity.Order;
import com.springteam.backend.entity.Product;
import com.springteam.backend.repository.IOrderRepository;
import com.springteam.backend.repository.IProductRepository;
import com.springteam.backend.service.impl.DashboardServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
public class DashboardServiceTest {
    private final String TOTAL_REVENUE = "TOTAL_REVENUE";
    private final String CURRENT_MONTH_REVENUE = "CRR_MONTH";
    private final String CURRENT_WEEK_REVENUE = "CRR_WEEK";
    private final String LAST_WEEK_REVENUE = "LAST_WEEK";
    @Mock
    private IOrderRepository orderRepository;
    @Mock
    private IProductRepository productRepository;
    @InjectMocks
    private DashboardServiceImpl dashboardService;

    @BeforeEach
    void beforeEach(){

    }

    @Test
    void getOrderQuantityTest(){
        when(orderRepository.count()).thenReturn(4L);
        assertEquals(4, dashboardService.getOrderQuantity());
    }

    @Test
    void getTotalRevenueTest(){
        List<Order> orderList = Arrays.asList(Order.builder().totalPrice(200L).build(),Order.builder().totalPrice(200L).build());

        long expectedValue = orderList.stream().map(Order::getTotalPrice).reduce(0L, (o1, o2) -> o1 + o2);
        when(orderRepository.getTotalRevenue()).thenReturn(expectedValue);

        assertEquals(expectedValue, dashboardService.getTotalRevenue());

        verify(orderRepository).getTotalRevenue();
    }

    @Test
    void getTotalRevenueChartTest(){

        DataChartDto iphoneData = DataChartDto.builder().name("iPhone").data(100L).build();
        DataChartDto ipadData = DataChartDto.builder().name("iPad").data(200L).build();
        DataChartDto macData = DataChartDto.builder().name("Mac").data(300L).build();
        List<DataChartDto> dataItemList = Arrays.asList(iphoneData, ipadData, macData);
        when(orderRepository.getTotalRevenueByCategory()).thenReturn(dataItemList);
        long expectedValue = dataItemList.stream().map(DataChartDto::getData).reduce(0L, Long::sum);

        assertEquals(dataItemList.size(), dashboardService.getRevenueDataChartByCategory(TOTAL_REVENUE).size());
        assertEquals(expectedValue, dashboardService.getRevenueDataChartByCategory(TOTAL_REVENUE).stream().map(DataChartDto::getData).reduce(0L, Long::sum));

        verify(orderRepository, times(2)).getTotalRevenueByCategory();
    }

    @Test
    void getRevenueChartByMonthTest(){

        DataChartDto iphoneData = DataChartDto.builder().name("iPhone").data(100L).build();
        DataChartDto ipadData = DataChartDto.builder().name("iPad").data(200L).build();
        DataChartDto macData = DataChartDto.builder().name("Mac").data(300L).build();
        List<DataChartDto> dataItemList = Arrays.asList(iphoneData, ipadData, macData);

        when(orderRepository.getRevenueByCategoryForCurrentMonth()).thenReturn(dataItemList);
        long expectedValue = dataItemList.stream().map(DataChartDto::getData).reduce(0L, Long::sum);

        assertEquals(dataItemList.size(), dashboardService.getRevenueDataChartByCategory(CURRENT_MONTH_REVENUE).size());
        assertEquals(expectedValue, dashboardService.getRevenueDataChartByCategory(CURRENT_MONTH_REVENUE).stream().map(DataChartDto::getData).reduce(0L, Long::sum));

        verify(orderRepository, times(2)).getRevenueByCategoryForCurrentMonth();
    }

    @Test
    void getRevenueChartByCurrentWeekTest(){

        DataChartDto iphoneData = DataChartDto.builder().name("iPhone").data(100L).build();
        DataChartDto ipadData = DataChartDto.builder().name("iPad").data(200L).build();
        DataChartDto macData = DataChartDto.builder().name("Mac").data(300L).build();
        List<DataChartDto> dataItemList = Arrays.asList(iphoneData, ipadData, macData);

        when(orderRepository.getRevenueByCategoryForCurrentWeek()).thenReturn(dataItemList);
        long expectedValue = dataItemList.stream().map(DataChartDto::getData).reduce(0L, Long::sum);

        assertEquals(dataItemList.size(), dashboardService.getRevenueDataChartByCategory(CURRENT_WEEK_REVENUE).size());
        assertEquals(expectedValue, dashboardService.getRevenueDataChartByCategory(CURRENT_WEEK_REVENUE).stream().map(DataChartDto::getData).reduce(0L, Long::sum));

        verify(orderRepository, times(2)).getRevenueByCategoryForCurrentWeek();
    }

    @Test
    void getRevenueChartByLastWeekTest(){

        DataChartDto iphoneData = DataChartDto.builder().name("iPhone").data(100L).build();
        DataChartDto ipadData = DataChartDto.builder().name("iPad").data(200L).build();
        DataChartDto macData = DataChartDto.builder().name("Mac").data(300L).build();
        List<DataChartDto> dataItemList = Arrays.asList(iphoneData, ipadData, macData);

        when(orderRepository.getRevenueByCategoryForLastWeek()).thenReturn(dataItemList);
        long expectedValue = dataItemList.stream().map(DataChartDto::getData).reduce(0L, Long::sum);

        assertEquals(dataItemList.size(), dashboardService.getRevenueDataChartByCategory(LAST_WEEK_REVENUE).size());
        assertEquals(expectedValue, dashboardService.getRevenueDataChartByCategory(LAST_WEEK_REVENUE).stream().map(DataChartDto::getData).reduce(0L, Long::sum));

        verify(orderRepository, times(2)).getRevenueByCategoryForLastWeek();
    }

    @Test
    void getTotalRevenueChartTest_ReturnEmptyList(){
        List<DataChartDto> emptyData = new ArrayList<>();
        emptyData.add(DataChartDto.builder().name("iPhone").data(0L).build());
        emptyData.add(DataChartDto.builder().name("iPad").data(0L).build());
        emptyData.add(DataChartDto.builder().name("Mac").data(0L).build());
        when(orderRepository.getTotalRevenueByCategory()).thenReturn(emptyData);
        long actualRevenue = dashboardService.getRevenueDataChartByCategory(TOTAL_REVENUE).stream().map(DataChartDto::getData).reduce(0L, Long::sum);
        assertEquals(0, actualRevenue);

        verify(orderRepository).getTotalRevenueByCategory();
    }

    @Test
    void getRevenueChartByCurrentMonthTest_ReturnEmptyList(){
        List<DataChartDto> emptyData = new ArrayList<>();
        emptyData.add(DataChartDto.builder().name("iPhone").data(0L).build());
        emptyData.add(DataChartDto.builder().name("iPad").data(0L).build());
        emptyData.add(DataChartDto.builder().name("Mac").data(0L).build());
        when(orderRepository.getRevenueByCategoryForCurrentMonth()).thenReturn(emptyData);
        long actualRevenue = dashboardService.getRevenueDataChartByCategory(CURRENT_MONTH_REVENUE).stream().map(DataChartDto::getData).reduce(0L, Long::sum);
        assertEquals(0, actualRevenue);

        verify(orderRepository).getRevenueByCategoryForCurrentMonth();
    }

    @Test
    void getRevenueChartByCurrentWeekTest_ReturnEmptyList(){
        List<DataChartDto> emptyData = new ArrayList<>();
        emptyData.add(DataChartDto.builder().name("iPhone").data(0L).build());
        emptyData.add(DataChartDto.builder().name("iPad").data(0L).build());
        emptyData.add(DataChartDto.builder().name("Mac").data(0L).build());
        when(orderRepository.getRevenueByCategoryForCurrentWeek()).thenReturn(emptyData);
        long actualRevenue = dashboardService.getRevenueDataChartByCategory(CURRENT_WEEK_REVENUE).stream().map(DataChartDto::getData).reduce(0L, Long::sum);
        assertEquals(0, actualRevenue);

        verify(orderRepository).getRevenueByCategoryForCurrentWeek();
    }

    @Test
    void getRevenueChartByLastWeekTest_ReturnEmptyList(){
        List<DataChartDto> emptyData = new ArrayList<>();
        emptyData.add(DataChartDto.builder().name("iPhone").data(0L).build());
        emptyData.add(DataChartDto.builder().name("iPad").data(0L).build());
        emptyData.add(DataChartDto.builder().name("Mac").data(0L).build());
        when(orderRepository.getRevenueByCategoryForLastWeek()).thenReturn(emptyData);
        long actualRevenue = dashboardService.getRevenueDataChartByCategory(LAST_WEEK_REVENUE).stream().map(DataChartDto::getData).reduce(0L, Long::sum);
        assertEquals(0, actualRevenue);

        verify(orderRepository).getRevenueByCategoryForLastWeek();
    }

    @Test
    void getProductQuantityTest(){
        Product product1 = Product.builder().id(1L).name("iphone 13").price(500L).build();
        Product product2 = Product.builder().id(2L).name("iphone 14").price(400L).build();
        Product product3 = Product.builder().id(3L).name("mac m2").price(300L).build();
        Product product4 = Product.builder().id(4L).name("mac m3").price(200L).build();
        List<Product> productList = Arrays.asList(product1, product2, product3, product4);

        when(productRepository.count()).thenReturn(Long.valueOf(productList.size()));

        assertEquals(4, dashboardService.getProductQuantity());

        verify(productRepository).count();
    }
}
