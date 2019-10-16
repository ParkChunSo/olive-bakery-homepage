package com.dev.olivebakery.domain.dtos.sales;

import com.dev.olivebakery.domain.enums.SaleType;
import lombok.Builder;
import lombok.Getter;

@Getter
public class AverageDto {
    private double ave;
    private SaleType saleType;

    @Builder

    public AverageDto(double ave, SaleType saleType) {
        this.ave = ave;
        this.saleType = saleType;
    }
}
