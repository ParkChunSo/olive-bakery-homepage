package com.dev.olivebakery.domain.dtos.reservation;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class ReservationInfoListResponseDto {

  private Long reservationId;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm", timezone = "Asia/Seoul")
  private LocalDateTime reservationTime;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm", timezone = "Asia/Seoul")
  private LocalDateTime bringTime;
  private int price;
  private String memberName;
  private List<ReservationBreadDto> reservationBreads = new ArrayList<>();

  @Builder
  public ReservationInfoListResponseDto(Long reservationId, LocalDateTime reservationTime,
      LocalDateTime bringTime, int price, String memberName,
      List<ReservationBreadDto> reservationBreads) {
    this.reservationId = reservationId;
    this.reservationTime = reservationTime;
    this.bringTime = bringTime;
    this.price = price;
    this.memberName = memberName;
    this.reservationBreads = reservationBreads;
  }

  public static ReservationInfoListResponseDto of(ReservationInfoListTmpDto reservationResponseTemp,
      List<ReservationBreadDto> reservationBreads) {
    return ReservationInfoListResponseDto.builder()
        .reservationId(reservationResponseTemp.getReservationId())
        .reservationTime(reservationResponseTemp.getReservationTime())
        .bringTime(reservationResponseTemp.getBringTime())
        .price(reservationResponseTemp.getPrice())
        .memberName(reservationResponseTemp.getMemberName())
        .reservationBreads(reservationBreads)
        .build();
  }

  @Override
  public String toString() {
    return "ReservationResponse{" +
        "reservationId=" + reservationId +
        ", reservationTime=" + reservationTime +
        ", bringTime=" + bringTime +
        ", price=" + price +
        ", memberName='" + memberName + '\'' +
        ", reservationBreads=" + reservationBreads +
        '}';
  }
}
