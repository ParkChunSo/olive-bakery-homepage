package com.dev.olivebakery.domain.daos;

import com.dev.olivebakery.domain.enums.BreadState;
import com.dev.olivebakery.domain.enums.DayType;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BreadListDao {
    //빵 uid
    private Long breadId;

    //빵 이름
    private String breadName;

    //빵 가격
    private Integer breadPrice;

    //상세정보가 아닌 간단한 소개(리스트에서 보내줄 것)
    private String description;

    //빵을 클릭했을 때 선택한 빵의 상세 소개
    private String detailDescription;

    //빵이 만들어지는 요일
    private String days;

    // 매진 여부
    private Boolean isSoldOut;

    //관리자가 선정한 빵 상태
    private BreadState state;

    // 빵을 만드는데 들어가는 재료와 각 재료의 원산지
    private String ingrediantName;

    private String ingrediantOrigin;

    //빵 사진 Url주소
    private String imageUrl;

    public BreadListDao(Long breadId, String breadName, Integer breadPrice, String description, String detailDescription, DayType days, Boolean isSoldOut, BreadState state, String ingrediantName, String ingrediantOrigin, String imageUrl) {
        this.breadId = breadId;
        this.breadName = breadName;
        this.breadPrice = breadPrice;
        this.description = description;
        this.detailDescription = detailDescription;
        if(days != null)
            this.days = days.name();
        this.isSoldOut = isSoldOut;
        this.state = state;
        this.ingrediantName = ingrediantName;
        this.ingrediantOrigin = ingrediantOrigin;
        this.imageUrl = imageUrl;
    }
}
