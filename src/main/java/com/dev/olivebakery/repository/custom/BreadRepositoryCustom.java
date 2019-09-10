package com.dev.olivebakery.repository.custom;

import com.dev.olivebakery.domain.dtos.bread.BreadListResponseDto;
import com.dev.olivebakery.domain.enums.DayType;

import java.util.List;

public interface BreadRepositoryCustom {
    List<BreadListResponseDto> getBreadListByDay(DayType day);
}
