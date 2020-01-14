package com.dev.olivebakery.domain.dtos.reservation;

import java.util.Objects;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReservationBreadDto {

  private String breadName;
  private int breadCount;

  @Builder
  public ReservationBreadDto(String breadName, int breadCount) {
    this.breadName = breadName;
    this.breadCount = breadCount;
  }

  public static ReservationBreadDto of(ReservationInfoListTmpDto reservationResponseTemp) {
    return ReservationBreadDto.builder()
        .breadName(reservationResponseTemp.getBreadName())
        .breadCount(reservationResponseTemp.getBreadCount())
        .build();
  }

  @Override
  public String toString() {
    return "ReservationBread{" +
        "breadName='" + breadName + '\'' +
        ", breadCount=" + breadCount +
        '}';
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }
    ReservationBreadDto that = (ReservationBreadDto) obj;
    return breadCount == that.breadCount &&
        Objects.equals(breadName, that.breadName);
  }

  @Override
  public int hashCode() {
    return Objects.hash(breadName, breadCount);
  }
}
