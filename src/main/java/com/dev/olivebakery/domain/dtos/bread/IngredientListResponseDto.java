package com.dev.olivebakery.domain.dtos.bread;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class IngredientListResponseDto {

    // 재료 이름
    private String name;

    // 재료 원산지
    private String origin;
}
