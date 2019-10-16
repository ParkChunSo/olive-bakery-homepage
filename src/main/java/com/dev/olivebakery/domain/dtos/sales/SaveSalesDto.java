package com.dev.olivebakery.domain.dtos.sales;

import com.dev.olivebakery.domain.entity.Sales;
import com.dev.olivebakery.domain.enums.SaleType;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter @NoArgsConstructor
@AllArgsConstructor
public class SaveSalesDto {
    @ApiModelProperty(notes = "2019-04-14 같은 형태.")
    private LocalDate date;
    private int sales;

    public Sales toEntity(){
        return Sales.builder()
                .date(date)
                .reservationCnt(0)
                .sales(sales)
                .saleType(SaleType.OFFLINE)
                .build();
    }
}
