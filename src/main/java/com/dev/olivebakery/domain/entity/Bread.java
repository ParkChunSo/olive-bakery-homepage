package com.dev.olivebakery.domain.entity;

import com.dev.olivebakery.domain.enums.BreadState;
import lombok.*;
import lombok.extern.java.Log;

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
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "bread")
    private List<Ingredients> ingredients = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "bread")
    private List<Days> days = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "bread")
    private List<BreadImage> images = new ArrayList<>();

    // 삭제 여부
    private Boolean isDeleted;

    // 매진 여부
    private Boolean isSoldOut;

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
        this.isDeleted = delete;
    }

    public void updateBreadIngredients(List<Ingredients> ingredientsList) {
        this.ingredients = ingredientsList;
    }

    public void addBreadIngredients(Ingredients ingredients) {
        this.ingredients.add(ingredients);
    }

    public void deleteBreadIngredients(Ingredients removeIngredients){
        this.ingredients.forEach(ingredients -> {
            if(ingredients.getName().equals(removeIngredients.getName()) && ingredients.getOrigin().equals(removeIngredients.getOrigin())){
                this.ingredients.remove(ingredients);
            }
        });
//        this.ingredients.remove(ingredients);
    }
}
