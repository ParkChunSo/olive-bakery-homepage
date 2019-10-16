package com.dev.olivebakery.domain.dtos.sales;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter  @Setter
@NoArgsConstructor
public class DashBoardDto {
    private LocalDate date;
    private int reservationCnt;
    private int reservationSale;
    private int offlineSale;
    private int totalSale;

    @Builder
    public DashBoardDto(LocalDate date, int reservationCnt, int reservationSale, int offlineSale, int totalSale) {
        this.date = date;
        this.reservationCnt = reservationCnt;
        this.reservationSale = reservationSale;
        this.offlineSale = offlineSale;
        this.totalSale = totalSale;
    }
}
