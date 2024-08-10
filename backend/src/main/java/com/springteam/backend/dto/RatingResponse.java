package com.springteam.backend.dto;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class RatingResponse {
    private List<ProductRatingDto> data;
    private int pageNumber;
    private int pageSize;
    private long totalElement;
    private int totalPage;
    private boolean isFirstPage;
    private boolean isLastPage;
}
