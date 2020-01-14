package com.dev.olivebakery.domain.dtos.bread;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

@Getter
public class IngredientListResponseDto {

  // 재료 이름
  private String name;

  // 재료 원산지
  private String origin;

  @Builder
  @JsonCreator
  public IngredientListResponseDto(@JsonProperty("name") String name,
      @JsonProperty("origin") String origin) {
    this.name = name;
    this.origin = origin;
  }
}
