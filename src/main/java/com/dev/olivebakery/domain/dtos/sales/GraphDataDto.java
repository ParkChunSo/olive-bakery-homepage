package com.dev.olivebakery.domain.dtos.sales;

import java.time.LocalDate;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
public class GraphDataDto {

  private LocalDate date;
  private double totalAve;

  List<AverageDto> byTypes;

  @Builder
  public GraphDataDto(LocalDate date, double totalAve, List<AverageDto> byTypes) {
    this.date = date;
    this.totalAve = totalAve;
    this.byTypes = byTypes;
  }
}
