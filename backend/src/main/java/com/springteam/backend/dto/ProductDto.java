package com.springteam.backend.dto;

import com.springteam.backend.entity.ProductCategory;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ProductDto {
    private Long id;
    private ProductCategoryDto category;
    private String name;
    private Long price;
    private String technicalDetails;
    private String color;
    private String description;
    private String image;
    private List<String> imageList;
}
