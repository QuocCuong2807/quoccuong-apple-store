package com.springteam.backend.dto;

import lombok.*;

import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class OrderDto {
    private long id;
    private String customerFullName;
    private String email;
    private String address;
    private String phoneNumber;
    private long totalPrice;
    private LocalDate date;
    private boolean status;
    private List<OrderDetailDto> orderDetails;
}
