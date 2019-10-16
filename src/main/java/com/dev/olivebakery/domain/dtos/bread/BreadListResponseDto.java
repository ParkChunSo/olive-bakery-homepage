package com.dev.olivebakery.domain.dtos.bread;

import com.dev.olivebakery.domain.enums.BreadState;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter @NoArgsConstructor
public class BreadListResponseDto {

    private Long id;
    //빵 이름
    private String name;

    //빵 가격
    private Integer price;

    //상세정보가 아닌 간단한 소개(리스트에서 보내줄 것)
    private String description;

    //빵을 클릭했을 때 선택한 빵의 상세 소개
    private String detailDescription;

    //빵이 만들어지는 요일
    private List<String> days = new ArrayList<>();

    // 매진 여부
    private Boolean isSoldOut;

    //관리자가 선정한 빵 상태
    private BreadState state;

    // 빵을 만드는데 들어가는 재료와 각 재료의 원산지
    private List<IngredientListResponseDto> ingredientsList = new ArrayList<>();

    //빵 사진 Url주소
    private String imageUrl;

    @Builder
    public BreadListResponseDto(Long breadId, String name, Integer price, String description, String detailDescription, List<String> days, Boolean isSoldOut, BreadState state, List<IngredientListResponseDto> ingredientsList, String imageUrl) {
        this.id = breadId;
        this.name = name;
        this.price = price;
        this.description = description;
        this.detailDescription = detailDescription;
        this.days = days;
        this.isSoldOut = isSoldOut;
        this.state = state;
        this.ingredientsList = ingredientsList;
        this.imageUrl = imageUrl;
    }
}