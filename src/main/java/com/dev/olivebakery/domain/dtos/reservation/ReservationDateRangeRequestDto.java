package com.dev.olivebakery.domain.dtos.reservation;

import com.dev.olivebakery.domain.enums.ReservationType;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReservationDateRangeRequestDto {

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
  public ReservationDateRangeRequestDto(ReservationType reservationType, LocalDate startDate,
      LocalDate endDate) {
    this.reservationType = reservationType;
    this.startDate = startDate;
    this.endDate = endDate;
  }
}
