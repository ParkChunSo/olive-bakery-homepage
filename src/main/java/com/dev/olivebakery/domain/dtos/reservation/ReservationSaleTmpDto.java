package com.dev.olivebakery.domain.dtos.reservation;

import com.dev.olivebakery.domain.entity.Sales;
import com.dev.olivebakery.domain.enums.SaleType;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReservationSaleTmpDto {
    private long reservationCount;
    private long reservationSales;
    private LocalDate date;

    @Builder
    public ReservationSaleTmpDto(long reservationCount, long reservationSales) {
        this.reservationCount = reservationCount;
        this.reservationSales = reservationSales;
        this.date = LocalDate.now();
    }

    public Sales toEntity() {
        return Sales.builder()
                .date(date)
                .reservationCnt((int) reservationCount)
                .sales((int) reservationSales)
                .saleType(SaleType.RESERVATION)
                .build();
    }
}
