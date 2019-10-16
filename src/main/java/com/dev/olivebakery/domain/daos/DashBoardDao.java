package com.dev.olivebakery.domain.daos;

import com.dev.olivebakery.domain.enums.SaleType;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class DashBoardDao {
    private LocalDate date;
    private int sales;
    private int reservationCnt;
    private SaleType saleType;

    @Builder
    public DashBoardDao(LocalDate date, int sales, int reservationCnt, SaleType saleType) {
        this.date = date;
        this.sales = sales;
        this.reservationCnt = reservationCnt;
        this.saleType = saleType;
    }
}
