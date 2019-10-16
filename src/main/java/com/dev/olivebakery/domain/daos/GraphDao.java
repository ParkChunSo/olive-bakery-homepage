package com.dev.olivebakery.domain.daos;

import com.dev.olivebakery.domain.enums.SaleType;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class GraphDao {
    private LocalDate date;
    private double ave;
    private SaleType saleType;

    @Builder
    public GraphDao(LocalDate date, double ave, SaleType saleType) {
        this.date = date;
        this.ave = ave;
        this.saleType = saleType;
    }
}
