package com.dev.olivebakery.domain.entity;

import com.dev.olivebakery.domain.dtos.bread.IngredientListResponseDto;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "bread_ingredient_tbl")
@Builder
public class Ingredients {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ingredients_id")
    private Long id;

    private String name;

    private String origin;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bread_id")
    private Bread bread;

    public static List<Ingredients> newListInstance(Bread bread, List<IngredientListResponseDto> ingredientsList) {
        List<Ingredients> ret = new ArrayList<>();
        for(IngredientListResponseDto dto : ingredientsList){
            ret.add(
                    Ingredients.builder().name(dto.getName()).origin(dto.getOrigin()).bread(bread).build()
            );
        }
        return ret;
    }
}