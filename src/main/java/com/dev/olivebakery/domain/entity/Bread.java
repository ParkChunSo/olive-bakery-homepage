package com.dev.olivebakery.domain.entity;

import com.dev.olivebakery.domain.enums.BreadState;
import com.dev.olivebakery.domain.enums.DayType;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

//TODO 빵이 지금 판매되고 있는지


@Entity
@Table(name = "bread_tbl")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
public class Bread {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bread_id")
    private Long breadId;

    @Column(unique = true)
    private String name;

    private Integer price;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "bread", fetch = FetchType.LAZY)
    private List<BreadImage> breadImages = new ArrayList<>();

    //상세정보가 아닌 간단한 소개(리스트에서 보내줄 것)
    private String description;

    //빵을 클릭했을 때 선택한 빵의 상세 소개
    private String detailDescription;

    // 매진 여부
    private Boolean isSoldOut;

    // 관리자가 선정한 빵 상태(추천, 등등)
    @Enumerated(value = EnumType.STRING)
    private BreadState state = BreadState.NEW;

    // 어떤 재료가 들어가며 재료의 원산지 표기
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "bread", fetch = FetchType.LAZY)
    private List<Ingredients> ingredients = new ArrayList<>();

    // 삭제 여부
    private Boolean deleteFlag;

    // 무슨 요일에 파는 빵인지
    //@ElementCollection(fetch = FetchType.EAGER)
    //private List<Days> days = new ArrayList<>();

    // 빵이 매진인지. soldout의 날짜가 오늘이면 매진
//    @OneToOne(fetch = FetchType.EAGER,mappedBy = "bread")
//    private SoldOut soldOut;



    public void updateName(String newName){
        this.name = newName;
    }

    public void updatePrice(int price){
        this.price = price;
    }

    public void updateDescription(String description){
        this.description = description;
    }

    public void updateDetailDescription(String detailDescription){
        this.detailDescription = detailDescription;
    }

    public void updateBreadState(BreadState breadState){
        this.state = breadState;
    }

    public void updateBreadSoldOut(boolean isSoldOut){
        this.isSoldOut = isSoldOut;
    }

    public void deleteBread(boolean delete){
        this.deleteFlag = delete;
    }

    public void addBreadIngredients(Ingredients ingredients) {
        this.ingredients.add(ingredients);
    }
}
