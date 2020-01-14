package com.dev.olivebakery.domain.entity;

import com.dev.olivebakery.domain.dtos.bread.IngredientListResponseDto;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

  public static List<Ingredients> newListInstance(Bread bread,
      List<IngredientListResponseDto> ingredientsList) {
    List<Ingredients> ret = new ArrayList<>();
    for (IngredientListResponseDto dto : ingredientsList) {
      ret.add(
          Ingredients.builder().name(dto.getName()).origin(dto.getOrigin()).bread(bread).build()
      );
    }
    return ret;
  }
}