package com.dev.olivebakery.domain.dtos;

import com.dev.olivebakery.domain.entity.Sales;
import com.dev.olivebakery.domain.enums.ReservationType;
import com.dev.olivebakery.domain.enums.SaleType;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * Created by YoungMan on 2019-03-06.
 */

public class ReservationDto {

  @Getter
  @NoArgsConstructor
  public static class ReservationResponse {

    private Long reservationId;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm", timezone = "Asia/Seoul")
    private LocalDateTime reservationTime;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm", timezone = "Asia/Seoul")
    private LocalDateTime bringTime;
    private int price;
    private String memberName;
    private List<ReservationBread> reservationBreads = new ArrayList<>();

    @Builder
    public ReservationResponse(Long reservationId, LocalDateTime reservationTime,
        LocalDateTime bringTime, int price, String memberName,
        List<ReservationBread> reservationBreads) {
      this.reservationId = reservationId;
      this.reservationTime = reservationTime;
      this.bringTime = bringTime;
      this.price = price;
      this.memberName = memberName;
      this.reservationBreads = reservationBreads;
    }

    public static ReservationResponse of(ReservationResponseTemp reservationResponseTemp,
        List<ReservationBread> reservationBreads) {
      return ReservationResponse.builder()
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


  @Getter
  @NoArgsConstructor(access = AccessLevel.PUBLIC)
  public static class ReservationResponseTemp {

    private Long reservationId;
    private LocalDateTime reservationTime;
    private LocalDateTime bringTime;
    private int price;
    private String memberName;
    private String breadName;
    private int breadCount;

    @Builder
    public ReservationResponseTemp(Long reservationId, LocalDateTime reservationTime,
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


  @Getter
  @NoArgsConstructor(access = AccessLevel.PROTECTED)
  public static class ReservationBread {

    private String breadName;
    private int breadCount;

    @Builder
    public ReservationBread(String breadName, int breadCount) {
      this.breadName = breadName;
      this.breadCount = breadCount;
    }

    public static ReservationBread of(ReservationResponseTemp reservationResponseTemp) {
      return ReservationBread.builder()
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
    public boolean equals(Object o) {
      if (this == o) {
        return true;
      }
      if (o == null || getClass() != o.getClass()) {
        return false;
      }
      ReservationBread that = (ReservationBread) o;
      return breadCount == that.breadCount &&
          Objects.equals(breadName, that.breadName);
    }

    @Override
    public int hashCode() {
      return Objects.hash(breadName, breadCount);
    }
  }


  @Getter
  @NoArgsConstructor(access = AccessLevel.PROTECTED)
  public static class ReservationSaveRequest {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
    @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
    private LocalDateTime bringTime;
    private List<ReservationBread> breadInfo = new ArrayList<>();

    @Builder
    public ReservationSaveRequest(LocalDateTime bringTime, List<ReservationBread> breadInfo) {
      this.bringTime = bringTime;
      this.breadInfo = breadInfo;
    }

    @ApiModelProperty(hidden = true)
    public List<String> getBreadNames() {
      return breadInfo.stream()
          .map(s -> s.getBreadName())
          .collect(Collectors.toList());
    }

    @ApiModelProperty(hidden = true)
    public List<Integer> getBreadCounts() {
      return breadInfo.stream()
          .map(s -> s.getBreadCount())
          .collect(Collectors.toList());
    }
  }

  @Getter
  @NoArgsConstructor(access = AccessLevel.PROTECTED)
  public static class ReservationUpdateRequest {

    private Long reservationId;
    private ReservationSaveRequest reservationSaveRequest;

    @Builder
    public ReservationUpdateRequest(Long reservationId,
        ReservationSaveRequest reservationSaveRequest) {
      this.reservationId = reservationId;
      this.reservationSaveRequest = reservationSaveRequest;
    }
  }


  @Getter
  @NoArgsConstructor(access = AccessLevel.PROTECTED)
  public static class ReservationDateRequest {

    private ReservationType reservationType;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
    @ApiModelProperty(notes = "2019-04-14 같은 형태.")
    private LocalDate selectDate;

    @Builder
    public ReservationDateRequest(ReservationType reservationType, LocalDate selectDate) {
      this.reservationType = reservationType;
      this.selectDate = selectDate;
    }
  }


  @Getter
  @NoArgsConstructor(access = AccessLevel.PROTECTED)
  public static class ReservationDateRangeRequest {

    private ReservationType reservationType;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
    @ApiModelProperty(notes = "2019-04-14 같은 형태.")
    private LocalDate startDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
    @ApiModelProperty(notes = "2019-04-14 같은 형태.")
    private LocalDate endDate;

    @Builder
    public ReservationDateRangeRequest(ReservationType reservationType, LocalDate startDate,
        LocalDate endDate) {
      this.reservationType = reservationType;
      this.startDate = startDate;
      this.endDate = endDate;
    }
  }


  @Getter
  @NoArgsConstructor(access = AccessLevel.PROTECTED)
  public static class ReservationSale {

    private long reservationCount;
    private long reservationSales;
    private LocalDate date;

    @Builder
    public ReservationSale(long reservationCount, long reservationSales) {
      this.reservationCount = reservationCount;
      this.reservationSales = reservationSales;
      this.date = LocalDate.now();
    }

    public Sales toEntity() {
      return Sales.builder()
          .date(date)
          .reservationCnt((int) reservationCount)
          .sales((int) reservationSales)
          .saleType(SaleType.RESERVATION)
          .build();
    }
  }


}
