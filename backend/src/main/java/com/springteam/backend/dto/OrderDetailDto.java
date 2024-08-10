package com.springteam.backend.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class OrderDetailDto {
    private long id;
    private ProductDto product;
    private int quantity;
    private long price;
}
