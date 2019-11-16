package com.dev.olivebakery.domain.dtos.reservation;

import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class ReservationInfoListTmpDto {

  private Long reservationId;
  private LocalDateTime reservationTime;
  private LocalDateTime bringTime;
  private int price;
  private String memberName;
  private String breadName;
  private int breadCount;

  @Builder
  public ReservationInfoListTmpDto(Long reservationId, LocalDateTime reservationTime,
      LocalDateTime bringTime, int price, String memberName, String breadName, int breadCount) {
    this.reservationId = reservationId;
    this.reservationTime = reservationTime;
    this.bringTime = bringTime;
    this.price = price;
    this.memberName = memberName;
    this.breadName = breadName;
    this.breadCount = breadCount;
  }

  @Override
  public String toString() {
    return "ReservationResponseTemp{" +
        "reservationId=" + reservationId +
        ", reservationTime=" + reservationTime +
        ", bringTime=" + bringTime +
        ", price=" + price +
        ", memberName='" + memberName + '\'' +
        ", breadName='" + breadName + '\'' +
        ", breadCount=" + breadCount +
        '}';
  }
}
