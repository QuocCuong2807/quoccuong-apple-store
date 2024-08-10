package com.springteam.backend.dto;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class OrderResponse {
    private List<OrderDto> data;
    private int pageNumber;
    private int pageSize;
    private long totalElement;
    private int totalPage;
    private boolean isFirstPage;
    private boolean isLastPage;
}
