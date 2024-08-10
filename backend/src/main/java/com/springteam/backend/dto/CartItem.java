package com.springteam.backend.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CartItem {
    private long id;
    private String name;
    private String image;
    private int quantity;
    private long price;

    public long getTotalPrice() {
        return this.price * this.quantity;
    }
}
