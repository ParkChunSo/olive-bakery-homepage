package com.dev.olivebakery.service;

import com.dev.olivebakery.domain.daos.DashBoardDao;
import com.dev.olivebakery.domain.daos.GraphDao;
import com.dev.olivebakery.domain.dtos.reservation.ReservationSaleTmpDto;
import com.dev.olivebakery.domain.dtos.sales.AverageDto;
import com.dev.olivebakery.domain.dtos.sales.DashBoardDto;
import com.dev.olivebakery.domain.dtos.sales.DeleteSalesDto;
import com.dev.olivebakery.domain.dtos.sales.GraphDataDto;
import com.dev.olivebakery.domain.dtos.sales.SaveSalesDto;
import com.dev.olivebakery.domain.entity.Sales;
import com.dev.olivebakery.domain.enums.SaleType;
import com.dev.olivebakery.exception.UserDefineException;
import com.dev.olivebakery.repository.SalesRepository;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import org.springframework.stereotype.Service;

@Service
public class SalesService {

  private final SalesRepository salesRepository;

  public SalesService(SalesRepository salesRepository) {
    this.salesRepository = salesRepository;
  }

  public List<GraphDataDto> getAverageAnnualSales() {
    List<GraphDao> averageSales = salesRepository
        .getAverageSales(DayType.YEAR.name(), LocalDate.now());
    if (averageSales.isEmpty()) {
      return null;
    }

    return convertGraphDao2Dto(averageSales);
  }

  public List<GraphDataDto> getAverageMonthlySales(int year) {
    List<GraphDao> averageSales = salesRepository
        .getAverageSales(DayType.MONTH.name(), LocalDate.of(year, 1, 1));
    if (averageSales.isEmpty()) {
      return null;
    }

    return convertGraphDao2Dto(averageSales);
  }

  public List<GraphDataDto> getDailySales(int year, int month) {
    List<GraphDao> averageSales = salesRepository
        .getAverageSales(DayType.DAY.name(), LocalDate.of(year, month, 1));
    if (averageSales.isEmpty()) {
      return null;
    }

    return convertGraphDao2Dto(averageSales);
  }

  public List<DashBoardDto> getDashData(int year, int month) {
    List<DashBoardDao> tblData = salesRepository.getDashData(LocalDate.of(year, month, 1));
    List<DashBoardDto> dashBoardDataList = new ArrayList<>();
    LocalDate localDate = tblData.get(0).getDate();
    DashBoardDto dashBoardData = new DashBoardDto();

    for (DashBoardDao tmpData : tblData) {
      if (!localDate.isEqual(tmpData.getDate())) {
        dashBoardData
            .setTotalSale(dashBoardData.getOfflineSale() + dashBoardData.getReservationSale());
        dashBoardData.setDate(localDate);
        dashBoardDataList.add(dashBoardData);
        dashBoardData = new DashBoardDto();
        localDate = tmpData.getDate();
      }

      if (SaleType.RESERVATION.name().equals(tmpData.getSaleType().name())) {
        dashBoardData.setReservationSale(tmpData.getSales());
        dashBoardData.setReservationCnt(tmpData.getReservationCnt());
      } else if (SaleType.OFFLINE.name().equals(tmpData.getSaleType().name())) {
        dashBoardData.setOfflineSale(tmpData.getSales());
      }
    }
    dashBoardData.setTotalSale(dashBoardData.getOfflineSale() + dashBoardData.getReservationSale());
    dashBoardData.setDate(localDate);
    dashBoardDataList.add(dashBoardData);
    return dashBoardDataList;
  }

  public void saveOfflineSale(SaveSalesDto sale) {
    if (!salesRepository.findByDateEqualsAndSaleType(sale.getDate(), SaleType.OFFLINE)
        .isPresent()) {
      salesRepository.save(sale.toEntity());
    } else {
      throw new UserDefineException("이미 저장되어 있는 매출정보가 있습니다.");
    }
  }

  public void saveReservationSale(ReservationSaleTmpDto sale) {
    if (!salesRepository.findByDateEqualsAndSaleType(sale.getDate(), SaleType.RESERVATION)
        .isPresent()) {
      salesRepository.save(sale.toEntity());
    } else {
      throw new UserDefineException("이미 저장되어 있는 매출정보가 있습니다.");
    }
  }

  public void updateSale(SaveSalesDto updateSales) {
    Sales sales = salesRepository
        .findByDateEqualsAndSaleType(updateSales.getDate(), SaleType.OFFLINE)
        .orElseThrow(() -> new UserDefineException("해당 매출 정보가 존재하지 않습니다."));
    sales.setSales(updateSales.getSales());
    sales.setDate(updateSales.getDate());
    salesRepository.save(sales);
  }

  public void deleteSale(DeleteSalesDto deleteSale) {
    Sales sales = salesRepository
        .findByDateEqualsAndSaleType(deleteSale.getDate(), SaleType.OFFLINE)
        .orElseThrow(() -> new UserDefineException("해당 매출 정보가 존재하지 않습니다."));
    salesRepository.delete(sales);
  }

  public SaveSalesDto getSaleInfo(int year, int month, int day) {
    Sales sales = salesRepository
        .findByDateEqualsAndSaleType(LocalDate.of(year, month, day), SaleType.OFFLINE)
        .orElseThrow(() -> new UserDefineException("해당 날짜의 매출 정보는 존재하지 않습니다."));
    return new SaveSalesDto(sales.getDate(), sales.getSales());
  }

  private List<GraphDataDto> convertGraphDao2Dto(List<GraphDao> daos) {
    List<GraphDataDto> graphData = new ArrayList<>();
    LocalDate localDate = daos.get(0).getDate();
    double total = 0;
    List<AverageDto> averageDtos = new ArrayList<>();

    for (GraphDao dao : daos) {
      if (!localDate.isEqual(dao.getDate())) {
        graphData.add(
            GraphDataDto.builder()
                .date(localDate)
                .totalAve(total)
                .byTypes(averageDtos)
                .build()
        );
        total = 0;
        averageDtos = new ArrayList<>();
        localDate = dao.getDate();
      }

      total += dao.getAve();
      averageDtos.add(
          AverageDto.builder()
              .ave(dao.getAve())
              .saleType(dao.getSaleType())
              .build()
      );
    }
    graphData.add(
        GraphDataDto.builder()
            .date(localDate)
            .totalAve(total)
            .byTypes(averageDtos)
            .build()
    );
    return graphData;
  }


  @Getter
  private enum DayType {
    YEAR, MONTH, DAY
  }

//    private List<SalesDto.GetGraphData> convertToGraphData(List<SalesDto.GetGraphTmp> tblData){
//        List<SalesDto.GetGraphData> graphData = new ArrayList<>();
//        LocalDate localDate = tblData.get(0).getDate();
//        double total = 0;
//        List<SalesDto.AverageByType> averageByTypes = new ArrayList<>();
//
//        for (SalesDto.GetGraphTmp tmpData : tblData) {
//            if(!localDate.isEqual(tmpData.getDate())) {
//                graphData.add(
//                        SalesDto.GetGraphData.builder()
//                                .date(localDate)
//                                .totalAve(total)
//                                .byTypes(averageByTypes)
//                                .build()
//                );
//                total = 0;
//                averageByTypes = new ArrayList<>();
//                localDate = tmpData.getDate();
//            }
//
//            total += tmpData.getAve();
//            averageByTypes.add(
//                    SalesDto.AverageByType.builder()
//                            .ave(tmpData.getAve())
//                            .saleType(tmpData.getSaleType())
//                            .build()
//            );
//        }
//        graphData.add(
//                SalesDto.GetGraphData.builder()
//                        .date(localDate)
//                        .totalAve(total)
//                        .byTypes(averageByTypes)
//                        .build()
//        );
//        return graphData;
//    }
}
