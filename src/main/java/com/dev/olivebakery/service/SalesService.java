package com.dev.olivebakery.service;

import com.dev.olivebakery.domain.dto.ReservationDto;
import com.dev.olivebakery.domain.dto.SalesDto;
import com.dev.olivebakery.domain.entity.Sales;
import com.dev.olivebakery.domain.enums.SaleType;
import com.dev.olivebakery.exception.UserDefineException;
import com.dev.olivebakery.repository.SalesRepository;
import lombok.Getter;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class SalesService {

    private final SalesRepository salesRepository;

    public SalesService(SalesRepository salesRepository) {
        this.salesRepository = salesRepository;
    }

    public List<SalesDto.GetGraphData> getAverageAnnualSales(){
        List<SalesDto.GetGraphTmp> averageSales = salesRepository.getAverageSales(DayType.YEAR.name(), LocalDate.now());
        if(averageSales.isEmpty())
            return null;

        return convertToGraphData(averageSales);
    }

    public List<SalesDto.GetGraphData> getAverageMonthlySales(int year) {
        List<SalesDto.GetGraphTmp> averageSales = salesRepository.getAverageSales(DayType.MONTH.name(), LocalDate.of(year, 1, 1));
        if(averageSales.isEmpty())
            return null;

        return convertToGraphData(averageSales);
    }

    public List<SalesDto.GetGraphData> getDailySales(int year, int month) {
        List<SalesDto.GetGraphTmp> averageSales = salesRepository.getAverageSales(DayType.DAY.name(), LocalDate.of(year, month, 1));
        if(averageSales.isEmpty())
            return null;

        return convertToGraphData(averageSales);
    }

    public List<SalesDto.GetDashBoardData> getDashData(int year, int month) {
        List<SalesDto.GetDashBoardTmp> tblData = salesRepository.getDashData(LocalDate.of(year, month, 1));
        List<SalesDto.GetDashBoardData> dashBoardDataList = new ArrayList<>();
        LocalDate localDate = tblData.get(0).getDate();
        SalesDto.GetDashBoardData dashBoardData = new SalesDto.GetDashBoardData();

        for (SalesDto.GetDashBoardTmp tmpData : tblData) {
            if(!localDate.isEqual(tmpData.getDate())) {
                dashBoardData.setTotalSale(dashBoardData.getOfflineSale()+dashBoardData.getReservationSale());
                dashBoardData.setDate(localDate);
                dashBoardDataList.add(dashBoardData);
                dashBoardData = new SalesDto.GetDashBoardData();
                localDate = tmpData.getDate();
            }

            if(SaleType.RESERVATION.name().equals(tmpData.getSaleType().name())){
                dashBoardData.setReservationSale(tmpData.getSales());
                dashBoardData.setReservationCnt(tmpData.getReservationCnt());
            }else if(SaleType.OFFLINE.name().equals(tmpData.getSaleType().name())){
                dashBoardData.setOfflineSale(tmpData.getSales());
            }
        }
        dashBoardData.setTotalSale(dashBoardData.getOfflineSale()+dashBoardData.getReservationSale());
        dashBoardData.setDate(localDate);
        dashBoardDataList.add(dashBoardData);
        return dashBoardDataList;
    }

    public void saveOfflineSale(SalesDto.SaveSale sale){
         if(!salesRepository.findByDateEqualsAndSaleType(sale.getDate(), SaleType.OFFLINE).isPresent())
             salesRepository.save(sale.toEntity());
         else
             throw new UserDefineException("이미 저장되어 있는 매출정보가 있습니다.");
    }

    public void saveReservationSale(ReservationDto.ReservationSale sale){
        if(!salesRepository.findByDateEqualsAndSaleType(sale.getDate(), SaleType.RESERVATION).isPresent())
            salesRepository.save(sale.toEntity());
        else
            throw new UserDefineException("이미 저장되어 있는 매출정보가 있습니다.");
    }
    
    public void updateSale(SalesDto.SaveSale updateSales){
        Sales sales = salesRepository.findByDateEqualsAndSaleType(updateSales.getDate(), SaleType.OFFLINE)
                .orElseThrow(() -> new UserDefineException("해당 매출 정보가 존재하지 않습니다."));
        sales.setSales(updateSales.getSales());
        sales.setDate(updateSales.getDate());
        salesRepository.save(sales);
    }

    public void deleteSale(SalesDto.DeleteSale deleteSale){
        Sales sales = salesRepository.findByDateEqualsAndSaleType(deleteSale.getDate(), SaleType.OFFLINE)
                .orElseThrow(() -> new UserDefineException("해당 매출 정보가 존재하지 않습니다."));
        salesRepository.delete(sales);
    }

    public SalesDto.SaveSale getSaleInfo(int year, int month, int day) {
        Sales sales = salesRepository.findByDateEqualsAndSaleType(LocalDate.of(year, month, day), SaleType.OFFLINE)
                .orElseThrow(() -> new UserDefineException("해당 날짜의 매출 정보는 존재하지 않습니다."));
        return new SalesDto.SaveSale(sales.getDate(), sales.getSales());
    }

    private List<SalesDto.GetGraphData> convertToGraphData(List<SalesDto.GetGraphTmp> tblData){
        List<SalesDto.GetGraphData> graphData = new ArrayList<>();
        LocalDate localDate = tblData.get(0).getDate();
        double total = 0;
        List<SalesDto.AverageByType> averageByTypes = new ArrayList<>();

        for (SalesDto.GetGraphTmp tmpData : tblData) {
            if(!localDate.isEqual(tmpData.getDate())) {
                graphData.add(
                        SalesDto.GetGraphData.builder()
                                .date(localDate)
                                .totalAve(total)
                                .byTypes(averageByTypes)
                                .build()
                );
                total = 0;
                averageByTypes = new ArrayList<>();
                localDate = tmpData.getDate();
            }

            total += tmpData.getAve();
            averageByTypes.add(
                    SalesDto.AverageByType.builder()
                            .ave(tmpData.getAve())
                            .saleType(tmpData.getSaleType())
                            .build()
            );
        }
        graphData.add(
                SalesDto.GetGraphData.builder()
                        .date(localDate)
                        .totalAve(total)
                        .byTypes(averageByTypes)
                        .build()
        );
        return graphData;
    }

    @Getter
    private enum DayType{
        YEAR, MONTH, DAY
    }
}
