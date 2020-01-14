package com.dev.olivebakery.domain.dtos.reservation;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReservationUpdateRequestDto {

  private Long reservationId;
  private ReservationSaveRequestDto reservationSaveRequest;

  @Builder
  public ReservationUpdateRequestDto(Long reservationId,
      ReservationSaveRequestDto reservationSaveRequest) {
    this.reservationId = reservationId;
    this.reservationSaveRequest = reservationSaveRequest;
  }
}
