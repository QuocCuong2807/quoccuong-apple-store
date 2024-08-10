package com.springteam.backend.service;

import com.springteam.backend.dto.*;
import com.springteam.backend.entity.Order;
import com.springteam.backend.entity.OrderDetail;
import com.springteam.backend.exception.OrderNotFoundException;
import com.springteam.backend.repository.IOrderRepository;
import com.springteam.backend.repository.IProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements IOrderService {
    private IOrderRepository orderRepository;
    private IProductRepository productRepository;
    private final String VN_PAY_METHOD = "VNPAY";

    @Autowired
    public OrderServiceImpl(IOrderRepository orderRepository, IProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
    }

    @Override
    public void saveOrder(CartDto cart, String province, String district, String ward) {
        StringBuilder addressStringBuilder = new StringBuilder();
        addressStringBuilder.append(cart.getAddress());
        addressStringBuilder.append(", ");
        addressStringBuilder.append(province);
        addressStringBuilder.append(", ");
        addressStringBuilder.append(district);
        addressStringBuilder.append(", ");
        addressStringBuilder.append(ward);

        Order order = Order.builder().address(addressStringBuilder.toString()).customerFullName(cart.getCustomerFullName()).email(cart.getEmail()).phoneNumber(cart.getPhoneNumber()).date(LocalDate.now()).totalPrice(cart.getCartTotalPrice()).build();

        List<OrderDetail> orderDetailList = cart.getCartItem().stream().map(item -> OrderDetail.builder().order(order).product(productRepository.findById(item.getId()).get()).productQuantity(item.getQuantity()).productPrice(item.getPrice()).build()).collect(Collectors.toList());

        order.setOrderDetails(orderDetailList);
        if (cart.getPaymentMethod().equals(VN_PAY_METHOD)) order.setPaid(true);
        else order.setPaid(false);

        orderRepository.save(order);
    }

    @Override
    public OrderResponse getAllOrders(int p, String statusFilter) {
        Pageable pageable = PageRequest.of(p, 5);
        Page<Order> page;
        if (!isDigit(statusFilter)) {
            page = orderRepository.findAll(pageable);
        } else {
            page = Integer.valueOf(statusFilter) == 1
                    ? orderRepository.findAllPaidOrder(pageable)
                    : orderRepository.findAllUnPaidOrder(pageable);
        }

        List<Order> orders = page.get().toList();
        List<OrderDto> orderDtoList = orders.stream()
                .map(order -> OrderDto.builder()
                        .id(order.getOrderId())
                        .totalPrice(order.getTotalPrice())
                        .phoneNumber(order.getPhoneNumber())
                        .date(order.getDate())
                        .status(order.isPaid())
                        .address(order.getAddress())
                        .email(order.getEmail())
                        .customerFullName(order.getCustomerFullName())
                        .orderDetails(order.getOrderDetails().stream()
                                .map(orderDetail -> OrderDetailDto.builder()
                                        .id(orderDetail.getId())
                                        .product(ProductDto.builder()
                                                .name(orderDetail.getProduct().getName())
                                                .color(orderDetail.getProduct().getColor())
                                                .technicalDetails(orderDetail.getProduct().getTechnicalDetails())
                                                .build())
                                        .quantity(orderDetail.getProductQuantity())
                                        .price(orderDetail.getProductPrice())
                                        .build())
                                .collect(Collectors.toList()))
                        .build())
                .collect(Collectors.toList());

        OrderResponse response = OrderResponse.builder()
                .data(orderDtoList)
                .isFirstPage(page.isFirst())
                .isLastPage(page.isLast())
                .pageNumber(page.getNumber())
                .pageSize(page.getSize())
                .totalElement(page.getTotalElements())
                .totalPage(page.getTotalPages())
                .build();

        return response;
    }

    @Override
    public OrderDto getOrderById(Long id) {
        Order order = orderRepository.findById(id).orElseThrow(() -> new OrderNotFoundException("cannot found order"));
        OrderDto orderDto = OrderDto.builder()
                .id(order.getOrderId())
                .phoneNumber(order.getPhoneNumber())
                .totalPrice(order.getTotalPrice())
                .date(order.getDate())
                .status(order.isPaid())
                .email(order.getEmail())
                .address(order.getAddress())
                .orderDetails(order.getOrderDetails().stream()
                        .map(orderDetail -> OrderDetailDto.builder()
                                .price(orderDetail.getProductPrice())
                                .id(orderDetail.getId())
                                .product(ProductDto.builder()
                                        .color(orderDetail.getProduct().getColor())
                                        .technicalDetails(orderDetail.getProduct().getTechnicalDetails())
                                        .name(orderDetail.getProduct().getName())
                                        .price(orderDetail.getProduct().getPrice())
                                        .build())
                                .quantity(orderDetail.getProductQuantity())
                                .build())
                        .collect(Collectors.toList()))
                .build();
        return orderDto;
    }


    private boolean isDigit(String s) {

        for (char c : s.toCharArray()) {
            if (!Character.isDigit(c)) return false;
        }

        return true;
    }
}
