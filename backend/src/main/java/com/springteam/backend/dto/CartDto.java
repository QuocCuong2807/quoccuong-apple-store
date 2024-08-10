package com.springteam.backend.dto;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CartDto {
    private String customerFullName;
    private String phoneNumber;
    private String email;
    private String address;
    private String paymentMethod;
    List<CartItem> cartItem;
    public long getCartTotalPrice() {
        return cartItem.stream()
                .map(item -> item.getTotalPrice())
                .reduce(Long.valueOf(0), (item1, item2) -> item1 + item2);
    }

}
