package com.springteam.backend.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Builder
@Getter
@Setter
public class ProductCategoryResponse {
    private List<ProductCategoryDto> data;
    private int pageNumber;
    private int pageSize;
    private long totalElement;
    private int totalPage;
    private boolean isFirstPage;
    private boolean isLastPage;
}
