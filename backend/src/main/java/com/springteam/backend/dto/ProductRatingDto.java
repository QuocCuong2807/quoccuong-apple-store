package com.springteam.backend.dto;

import com.springteam.backend.annotation.IsNumber;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ProductRatingDto {
    @NotNull
    @NotBlank
    private String userName;
    @NotNull
    @Min(0)
    @Max(5)
    private int star;
    @NotNull
    @NotBlank
    private String ratingContent;
    @NotNull
    @Min(1)
    private Long productId;
    @NotNull
    private String date;
}
