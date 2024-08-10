package com.springteam.backend.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class ProductOverviewResponse {
    private Long categoryId;
    private String categoryName;
    private List<ProductDto> productDtoList;
}
