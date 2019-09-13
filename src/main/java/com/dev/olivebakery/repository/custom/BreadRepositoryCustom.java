package com.dev.olivebakery.repository.custom;

import com.dev.olivebakery.domain.daos.BreadListDao;
import com.dev.olivebakery.domain.enums.DayType;

import java.util.List;

public interface BreadRepositoryCustom {
    List<BreadListDao> getAllBreadList();
    List<BreadListDao> getBreadListByDay(DayType day);
    List<BreadListDao> getBreadByBreadName(String breadName);
    List<String> getImagePathByBreadName(String breadName);
}
