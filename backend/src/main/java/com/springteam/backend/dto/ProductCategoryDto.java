package com.springteam.backend.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ProductCategoryDto {
    private Long id;
    private String name;
    private String description;
}
