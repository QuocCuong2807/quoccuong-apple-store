package com.springteam.backend.repository;

import com.springteam.backend.dto.DataChartDto;
import com.springteam.backend.entity.Order;
import jakarta.persistence.NamedNativeQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IOrderRepository extends JpaRepository<Order, Long> {
    @Query("SELECT o FROM Order o WHERE o.isPaid = true ORDER BY o.orderId")
    Page<Order> findAllPaidOrder(Pageable pageable);

    @Query("SELECT o FROM Order o WHERE o.isPaid = false ORDER BY o.orderId")
    Page<Order> findAllUnPaidOrder(Pageable pageable);

    @Query("SELECT SUM(o.totalPrice)FROM Order o")
    long getTotalRevenue();

    //dữ liệu biểu đồ tổng doanh thu theo loại
    @Query("SELECT new com.springteam.backend.dto.DataChartDto(p.category.name, COALESCE(SUM(od.productPrice * od.productQuantity), 0))" +
            "FROM OrderDetail od " +
            "JOIN Product p on od.product.id = p.id " +
            "GROUP BY p.category.name")
    List<DataChartDto> getTotalRevenueByCategory();

    //dữ liệu biểu đồ doanh thu trong tuần
    @Query("SELECT new com.springteam.backend.dto.DataChartDto(p.category.name, COALESCE(SUM(od.productPrice * od.productQuantity), 0))" +
            "FROM OrderDetail od " +
            "JOIN Product p on od.product.id = p.id " +
            "WHERE FUNCTION('YEARWEEK', od.order.date, 1) = FUNCTION('YEARWEEK', CURRENT_DATE, 1)" +
            "GROUP BY p.category.name")
    List<DataChartDto> getRevenueByCategoryForCurrentWeek();

    //dữ liệu biểu đồ doanh thu tuần trước
    @Query(name = "Order.getLastWeekRevenue", nativeQuery = true)
    List<DataChartDto> getRevenueByCategoryForLastWeek();

    //dữ liệu biểu đồ doanh thu trong tháng
    @Query("SELECT new com.springteam.backend.dto.DataChartDto(p.category.name, COALESCE(SUM(od.productPrice * od.productQuantity), 0)) " +
            "FROM OrderDetail od " +
            "RIGHT JOIN Product p ON od.product.id = p.id " +
            "AND FUNCTION('MONTH', od.order.date) = FUNCTION('MONTH', CURRENT_DATE) " +
            "AND FUNCTION('YEAR', od.order.date) = FUNCTION('YEAR', CURRENT_DATE) " +
            "GROUP BY p.category.name")
    List<DataChartDto> getRevenueByCategoryForCurrentMonth();

}
