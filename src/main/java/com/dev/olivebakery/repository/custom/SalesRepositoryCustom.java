package com.dev.olivebakery.repository.custom;

import com.dev.olivebakery.domain.daos.DashBoardDao;
import com.dev.olivebakery.domain.daos.GraphDao;
import com.dev.olivebakery.domain.dtos.SalesDto;

import java.time.LocalDate;
import java.util.List;

public interface SalesRepositoryCustom {
    List<GraphDao> getAverageSales(String dayType, LocalDate date);
    List<DashBoardDao> getDashData(LocalDate date);
}
