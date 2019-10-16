package com.dev.olivebakery.controller;

import com.dev.olivebakery.domain.dtos.SalesDto;
import com.dev.olivebakery.domain.dtos.sales.DashBoardDto;
import com.dev.olivebakery.domain.dtos.sales.DeleteSalesDto;
import com.dev.olivebakery.domain.dtos.sales.GraphDataDto;
import com.dev.olivebakery.domain.dtos.sales.SaveSalesDto;
import com.dev.olivebakery.service.SalesService;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/olive/sales")
public class SalesController {

    private final SalesService salesService;

    public SalesController(SalesService salesService) {
        this.salesService = salesService;
    }

    @ApiOperation("년도별 평균 매출액 조회")
    @GetMapping("/graph")
    public List<GraphDataDto> getAverageAnnualSales(){
        return salesService.getAverageAnnualSales();
    }

    @ApiOperation("월별 평균 매출액 조회")
    @GetMapping("/graph/year/{year}")
    public List<GraphDataDto> getAverageMonthlySales(@PathVariable("year") int year){
        return salesService.getAverageMonthlySales(year);
    }

    @ApiOperation("일별 평균 매출액 조회")
    @GetMapping("/graph/year/{year}/month/{month}")
    public List<GraphDataDto> getDailySales(@PathVariable("year") int year, @PathVariable("month") int month){
        return salesService.getDailySales(year, month);
    }

    @ApiOperation("대시보드 데이터 조회")
    @GetMapping("/dash/year/{year}/month/{month}")
    public List<DashBoardDto> getDashData(@PathVariable("year") int year, @PathVariable("month") int month){
        return salesService.getDashData(year, month);
    }

    @ApiOperation("오프라인 하루 매출정보 조회")
    @GetMapping("/year/{year}/month/{month}/day/{day}")
    public SaveSalesDto getSaleInfo(@PathVariable("year") int year, @PathVariable("month") int month, @PathVariable("day") int day){
        return salesService.getSaleInfo(year, month, day);
    }

    @ApiOperation("오프라인 매출정보 저장")
    @PostMapping
    public void saveSaleInfo(@RequestBody SaveSalesDto saveSale){
        salesService.saveOfflineSale(saveSale);
    }

    @ApiOperation("오프라인 매출 정보 수정")
    @PutMapping
    public void updateSaleInfo(@RequestBody SaveSalesDto saveSale){
        salesService.updateSale(saveSale);
    }

    @ApiOperation("매출 정보 삭제")
    @DeleteMapping
    public void deleteSaleInfo(@RequestBody DeleteSalesDto deleteSale){
        salesService.deleteSale(deleteSale);
    }
}
