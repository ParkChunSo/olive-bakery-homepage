package com.dev.olivebakery.domain.dtos.bread;

import com.dev.olivebakery.domain.entity.Bread;
import com.dev.olivebakery.domain.entity.Days;
import com.dev.olivebakery.domain.entity.Ingredients;
import com.dev.olivebakery.domain.enums.BreadState;
import com.dev.olivebakery.domain.enums.DayType;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter @NoArgsConstructor
public class BreadRequestDto {
    //빵 이름
    private String name;

    //빵 가격
    private Integer price;

    //상세정보가 아닌 간단한 소개(리스트에서 보내줄 것)
    private String description;

    //빵을 클릭했을 때 선택한 빵의 상세 소개
    private String detailDescription;

    //빵이 만들어지는 요일
    private List<DayType> days = new ArrayList<>();

    //관리자가 선정한 빵 상태
    private BreadState state = BreadState.NORMAL;

    // 빵을 만드는데 들어가는 재료와 각 재료의 원산지
    private List<IngredientListResponseDto> ingredientsList = new ArrayList<>();

    public Bread toEntity(){
        return Bread.builder()
                .name(this.name)
                .state(this.state)
                .price(this.price)
                .description(this.description)
                .detailDescription(this.detailDescription)
                .days(Days.newListInstance(this.days))
                .ingredients(Ingredients.newListInstance(this.ingredientsList))
                .isSoldOut(false)
                .isDeleted(false)
                .build();
    }
}
