package com.springteam.backend.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class StarRatingOverviewDto {
    private String label;
    private int quantity;
}
