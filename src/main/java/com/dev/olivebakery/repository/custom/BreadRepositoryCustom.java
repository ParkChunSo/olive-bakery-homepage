package com.dev.olivebakery.repository.custom;

import com.dev.olivebakery.domain.daos.BreadListDao;
import com.dev.olivebakery.domain.dtos.bread.BreadListResponseDto;
import com.dev.olivebakery.domain.enums.DayType;

import java.util.List;

public interface BreadRepositoryCustom {
    List<BreadListDao> getBreadListByDay(DayType day);
    List<String> getImagePathByBreadName(String breadName);
}
