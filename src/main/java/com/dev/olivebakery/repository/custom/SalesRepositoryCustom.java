package com.dev.olivebakery.repository.custom;

import com.dev.olivebakery.domain.dtos.SalesDto;

import java.time.LocalDate;
import java.util.List;

public interface SalesRepositoryCustom {
    List<SalesDto.GetGraphTmp> getAverageSales(String dayType, LocalDate date);
    List<SalesDto.GetDashBoardTmp> getDashData(LocalDate date);
}
