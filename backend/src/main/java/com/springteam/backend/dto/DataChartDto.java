package com.springteam.backend.dto;

import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor
@Builder
@Getter
@Setter
public class DataChartDto {
    private String name;
    private long data;

    public DataChartDto(String name, long data) {
        this.name = name;
        this.data = data;
    }
}
