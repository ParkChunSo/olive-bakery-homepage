package com.dev.olivebakery.domain.entity;

import com.dev.olivebakery.domain.enums.BreadState;
import lombok.*;
import lombok.extern.java.Log;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "bread_tbl")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
@Log
public class Bread {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bread_id")
    private Long breadId;

    @Column(unique = true)
    private String name;

    private Integer price;

    //상세정보가 아닌 간단한 소개(리스트에서 보내줄 것)
    private String description;

    //빵을 클릭했을 때 선택한 빵의 상세 소개
    @Column(length = 2000)
    private String detailDescription;

    // 관리자가 선정한 빵 상태(추천, 등등)1
    @Enumerated(value = EnumType.STRING)
    private BreadState state = BreadState.NEW;

    // 어떤 재료가 들어가며 재료의 원산지 표기
    /*    @ManyToMany(fetch = FetchType.EAGER, cascade={CascadeType.PERSIST,CascadeType.MERGE})
        @JsonManagedReference
        @JoinTable(
                name = "bread_ingredients",
                joinColumns = @JoinColumn(name = "bread_id"),
                inverseJoinColumns = @JoinColumn(name = "ingredients_id")
        )*/
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "bread", cascade = {CascadeType.MERGE, CascadeType.REMOVE})
    private List<Ingredients> ingredients = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "bread", cascade = {CascadeType.MERGE, CascadeType.REMOVE})
    private List<Days> days = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "bread", cascade = CascadeType.MERGE)
    private List<BreadImage> images = new ArrayList<>();

    // 삭제 여부
    private Boolean isDeleted;

    // 매진 여부
    private Boolean isSoldOut;

    public void deleteBread(boolean delete){
        this.isDeleted = delete;
    }

    public void updateBreadState(BreadState breadState){
        this.state = breadState;
    }

    public void updateBreadInfo(Integer price, String description, String detailDescription){
        this.price = price;
        this.description = description;
        this.detailDescription = detailDescription;
    }

    public void updateBreadSoldOut(boolean isSoldOut){
        this.isSoldOut = isSoldOut;
    }

    public void updateBreadIngredients(List<Ingredients> ingredientsList) {
        this.ingredients = ingredientsList;
    }

    public void updateDays(List<Days> days){this.days = days;}

    public void addBreadImages(BreadImage image){this.images.add(image);}

    public void updateBreadImages(List<BreadImage> images){this.images = images;}
}
