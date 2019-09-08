package com.dev.olivebakery.repository.custom;

import com.dev.olivebakery.domain.dtos.reservation.ReservationInfoListResponseDto;
import com.dev.olivebakery.domain.dtos.reservation.ReservationInfoListTmpDto;
import com.dev.olivebakery.domain.dtos.reservation.ReservationSaleTmpDto;
import com.dev.olivebakery.domain.enums.ReservationType;

import java.time.LocalDateTime;
import java.util.List;

public interface ReservationRepositoryCustom {
    List<ReservationInfoListTmpDto> getReservationInfoList(String id, ReservationType type);
    List<ReservationInfoListTmpDto> getReservationInfoListByRecentlyDate(String id);
    List<ReservationInfoListTmpDto> getReservationInfoListByDate(LocalDateTime startDate, LocalDateTime endDate, ReservationType reservationType);
    ReservationSaleTmpDto getReservationSaleByDate(LocalDateTime startDate, LocalDateTime endDate, ReservationType reservationType);
    String getMemberIdByReservationId(Long reservationId);
}
