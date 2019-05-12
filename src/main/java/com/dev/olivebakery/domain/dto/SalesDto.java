package com.dev.olivebakery.domain.dto;

import com.dev.olivebakery.domain.entity.Sales;
import com.dev.olivebakery.domain.enums.SaleType;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

public class SalesDto {

    @Getter
    @NoArgsConstructor
    public static class GetGraphTmp {
        private LocalDate date;
        private double ave;
        private SaleType saleType;

        @Builder
        public GetGraphTmp(LocalDate date, double ave, SaleType saleType) {
            this.date = date;
            this.ave = ave;
            this.saleType = saleType;
        }
    }

    @Getter
    @NoArgsConstructor
    public static class GetGraphData{
        private LocalDate date;
        private double totalAve;

        List<AverageByType> byTypes;

        @Builder
        public GetGraphData(LocalDate date, double totalAve, List<AverageByType> byTypes) {
            this.date = date;
            this.totalAve = totalAve;
            this.byTypes = byTypes;
        }
    }

    @Getter
    @NoArgsConstructor
    public static class AverageByType{
        private double ave;
        private SaleType saleType;

        @Builder
        public AverageByType(double ave, SaleType saleType) {
            this.ave = ave;
            this.saleType = saleType;
        }
    }

    @Getter @Setter
    @NoArgsConstructor
    public static class GetDashBoardData{
        private LocalDate date;
        private int reservationCnt;
        private int reservationSale;
        private int offlineSale;
        private int totalSale;

        @Builder
        public GetDashBoardData(LocalDate date, int reservationCnt, int reservationSale, int offlineSale, int totalSale) {
            this.date = date;
            this.reservationCnt = reservationCnt;
            this.reservationSale = reservationSale;
            this.offlineSale = offlineSale;
            this.totalSale = totalSale;
        }
    }

    @Getter
    @NoArgsConstructor
    public static class GetDashBoardTmp{
        private LocalDate date;
        private int sales;
        private int reservationCnt;
        private SaleType saleType;

        public GetDashBoardTmp(LocalDate date, int sales, int reservationCnt, SaleType saleType) {
            this.date = date;
            this.sales = sales;
            this.reservationCnt = reservationCnt;
            this.saleType = saleType;
        }
    }

    @Getter
    @NoArgsConstructor @AllArgsConstructor
    public static class SaveSale{
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

    @Getter
    @NoArgsConstructor @AllArgsConstructor
    public static class tete<T>{
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

    @Getter
    @NoArgsConstructor
    public static class DeleteSale{
        @ApiModelProperty(notes = "2019-04-14 같은 형태.")
        private LocalDate date;
        private SaleType saleType;
    }
}
