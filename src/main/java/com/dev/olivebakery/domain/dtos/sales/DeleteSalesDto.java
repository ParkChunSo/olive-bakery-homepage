package com.dev.olivebakery.domain.dtos.sales;

import com.dev.olivebakery.domain.enums.SaleType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class DeleteSalesDto {
    @ApiModelProperty(notes = "2019-04-14 같은 형태.")
    private LocalDate date;
    private SaleType saleType;
}
