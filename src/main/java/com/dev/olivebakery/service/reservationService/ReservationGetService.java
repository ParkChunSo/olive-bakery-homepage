package com.dev.olivebakery.service.reservationService;

import com.dev.olivebakery.domain.dtos.reservation.ReservationDateRangeRequestDto;
import com.dev.olivebakery.domain.dtos.reservation.ReservationDateRequestDto;
import com.dev.olivebakery.domain.dtos.reservation.ReservationInfoListResponseDto;
import com.dev.olivebakery.domain.dtos.reservation.ReservationInfoListTmpDto;
import com.dev.olivebakery.domain.entity.Reservation;
import com.dev.olivebakery.domain.enums.MemberRole;
import com.dev.olivebakery.domain.enums.ReservationType;
import com.dev.olivebakery.exception.UserDefineException;
import com.dev.olivebakery.repository.ReservationRepository;
import com.dev.olivebakery.security.JwtProvider;
import com.dev.olivebakery.utill.ConverterUtils;
import com.dev.olivebakery.utill.DateUtils;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

/**
 * Created by YoungMan on 2019-05-21.
 */

@Service
@RequiredArgsConstructor
public class ReservationGetService {

  private final ReservationRepository reservationRepository;
  private final JwtProvider jwtProvider;


  public Reservation findById(Long reservationId) {
    return reservationRepository.findById(reservationId)
        .orElseThrow(() -> new UserDefineException("해당 예약내역이 없습니다."));
  }

  /**
   * 유저의 모든 예약내역을 예약타입별로 가져옴
   */
  public List<ReservationInfoListResponseDto> getReservationInfos(ReservationType reservationType,
      String bearerToken) {
    String email = jwtProvider.getUserEmailByToken(bearerToken);
    List<ReservationInfoListTmpDto> reservationInfoListTmpDtoList = reservationRepository
        .getReservationInfoList(email,
            reservationType);

    if (ObjectUtils.isEmpty(reservationInfoListTmpDtoList)) {
      return new ArrayList<>();
    }
    return ConverterUtils.convertGetTempDtoListToGetDtoList(reservationInfoListTmpDtoList);
  }

  /**
   * 유저의 모든 예약내역을 예약타입별로 가져옴
   */
  public List<ReservationInfoListResponseDto> getReservationInfosByUserId(String id,
      ReservationType reservationType, String bearerToken) {
    List<MemberRole> roles = jwtProvider.getUserRolesByToken(bearerToken);
    if (!roles.contains(MemberRole.ADMIN)) {
      throw new UserDefineException("해당권한이 없습니다.", HttpStatus.UNAUTHORIZED);
    }

    List<ReservationInfoListTmpDto> reservationInfoListTmpDtoList = reservationRepository
        .getReservationInfoList(id,
            reservationType);

    if (ObjectUtils.isEmpty(reservationInfoListTmpDtoList)) {
      return new ArrayList<>();
    }
    return ConverterUtils.convertGetTempDtoListToGetDtoList(reservationInfoListTmpDtoList);
  }

  /**
   * 유저의 가장 최근 예약내역을 예약타입에 무관하게 조회
   */
  public ReservationInfoListResponseDto getReservationInfoByRecently(String bearerToken) {
    String email = jwtProvider.getUserEmailByToken(bearerToken);
    List<ReservationInfoListTmpDto> infoListTmpDtoList = reservationRepository
        .getReservationInfoListByRecentlyDate(email);

    if (ObjectUtils.isEmpty(infoListTmpDtoList)) {
      return new ReservationInfoListResponseDto();
    }
    return ConverterUtils.convertGetTmpDtoToGetDto(infoListTmpDtoList);
  }

  /**
   * 날짜별 예약 조회, Admin 에서 Role
   */
  public List<ReservationInfoListResponseDto> getReservationInfosByDate(
      ReservationDateRequestDto reservationDateRequest,
      String bearerToken) {
    checkValidateRole(bearerToken);
    List<ReservationInfoListTmpDto> reservationResponseTemps = reservationRepository
        .getReservationInfoListByDate(
            DateUtils.getStartOfDay(reservationDateRequest.getSelectDate()),
            DateUtils.getEndOfDay(reservationDateRequest.getSelectDate()),
            reservationDateRequest.getReservationType()
        );

    if (ObjectUtils.isEmpty(reservationResponseTemps)) {
      return new ArrayList<>();
    }
    return ConverterUtils.convertGetTempDtoListToGetDtoList(reservationResponseTemps);
  }

  /**
   * 날짜구간별 예약 조회, Admin Role
   */
  public List<ReservationInfoListResponseDto> getReservationInfosByDateRange(
      ReservationDateRangeRequestDto reservationDateRangeRequest,
      String bearerToken) {
    checkValidateRole(bearerToken);
    List<ReservationInfoListTmpDto> reservationResponseTemps = reservationRepository
        .getReservationInfoListByDate(
            DateUtils.getStartOfDay(reservationDateRangeRequest.getStartDate()),
            DateUtils.getEndOfDay(reservationDateRangeRequest.getEndDate()),
            reservationDateRangeRequest.getReservationType()
        );

    if (ObjectUtils.isEmpty(reservationResponseTemps)) {
      return new ArrayList<>();
    }
    return ConverterUtils.convertGetTempDtoListToGetDtoList(reservationResponseTemps);
  }

  private void checkValidateRole(String bearerToken) {
    List<MemberRole> roles = jwtProvider.getUserRolesByToken(bearerToken);
    if (!roles.contains(MemberRole.ADMIN)) {
      throw new UserDefineException("관리자 권한이 아닙니다");
    }
  }
}
