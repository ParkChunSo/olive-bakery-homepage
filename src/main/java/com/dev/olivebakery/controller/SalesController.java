package com.dev.olivebakery.controller;

import com.dev.olivebakery.domain.dtos.SalesDto;
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
    public List<SalesDto.GetGraphData> getAverageAnnualSales(){
        return salesService.getAverageAnnualSales();
    }

    @ApiOperation("월별 평균 매출액 조회")
    @GetMapping("/graph/year/{year}")
    public List<SalesDto.GetGraphData> getAverageMonthlySales(@PathVariable("year") int year){
        return salesService.getAverageMonthlySales(year);
    }

    @ApiOperation("일별 평균 매출액 조회")
    @GetMapping("/graph/year/{year}/month/{month}")
    public List<SalesDto.GetGraphData> getDailySales(@PathVariable("year") int year, @PathVariable("month") int month){
        return salesService.getDailySales(year, month);
    }

    @ApiOperation("대시보드 데이터 조회")
    @GetMapping("/dash/year/{year}/month/{month}")
    public List<SalesDto.GetDashBoardData> getDashData(@PathVariable("year") int year, @PathVariable("month") int month){
        return salesService.getDashData(year, month);
    }

    @ApiOperation("오프라인 하루 매출정보 조회")
    @GetMapping("/year/{year}/month/{month}/day/{day}")
    public SalesDto.SaveSale getSaleInfo(@PathVariable("year") int year, @PathVariable("month") int month, @PathVariable("day") int day){
        return salesService.getSaleInfo(year, month, day);
    }

    @ApiOperation("오프라인 매출정보 저장")
    @PostMapping
    public void saveSaleInfo(@RequestBody SalesDto.SaveSale saveSale){
        salesService.saveOfflineSale(saveSale);
    }

    @ApiOperation("오프라인 매출 정보 수정")
    @PutMapping
    public void updateSaleInfo(@RequestBody SalesDto.SaveSale saveSale){
        salesService.updateSale(saveSale);
    }

    @ApiOperation("매출 정보 삭제")
    @DeleteMapping
    public void deleteSaleInfo(@RequestBody SalesDto.DeleteSale deleteSale){
        salesService.deleteSale(deleteSale);
    }
}
