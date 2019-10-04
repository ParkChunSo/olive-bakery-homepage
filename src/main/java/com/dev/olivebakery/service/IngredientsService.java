package com.dev.olivebakery.service;

import com.dev.olivebakery.domain.dtos.BreadDto;
import com.dev.olivebakery.domain.entity.Ingredients;
import com.dev.olivebakery.exception.UserDefineException;
import com.dev.olivebakery.repository.IngredientsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class IngredientsService {

    private final IngredientsRepository ingredientsRepository;

    /* 성분 하나 추가 */
    public Ingredients saveIngredients(BreadDto.BreadIngredient ingredientsDto) {
        Ingredients newIngredients = ingredientsBuilder(ingredientsDto);

        return checkIngredientsRepository(newIngredients);
}

    /* 성분 여러 개 추가 */
    public List<Ingredients> saveIngredientsList(List<BreadDto.BreadIngredient> ingredientsDtoList) {
        List<Ingredients> ingredientsList = new ArrayList<>();

        ingredientsDtoList.forEach(ingredientsDto -> {
            ingredientsList.add(checkIngredientsRepository(ingredientsBuilder(ingredientsDto)));
        });

        return ingredientsList;
    }

    /* 성분 삭제 */
    public Ingredients deleteIngredients(BreadDto.BreadIngredient ingredientsDto) {
        Ingredients deleteIngredients = Optional.ofNullable(ingredientsRepository.
                findByNameAndOrigin(ingredientsDto.getName(), ingredientsDto.getOrigin()))
            .orElseThrow(() -> new UserDefineException("해당 성분이 존재하지 않습니다."));

        ingredientsRepository.delete(deleteIngredients);

        return deleteIngredients;
    }

    /* 성분 모두 가져오기 */
    public List<BreadDto.BreadIngredient> getIngredientsList(){
          List<Ingredients> ingredientsList = ingredientsRepository.findAll();

          return ingredientListToIngredientsDtoList(ingredientsList);
    }

    /* 성분 리스트 -> 성분 디티오 리스트 */
    private List<BreadDto.BreadIngredient> ingredientListToIngredientsDtoList(List<Ingredients> ingredientsList){
        List<BreadDto.BreadIngredient> ingredientDtoList = new ArrayList<>();

        ingredientsList.forEach(ingredients -> {
            ingredientDtoList.add(BreadDto.BreadIngredient.builder().name(ingredients.getName()).origin(ingredients.getOrigin()).build());
        });

        return ingredientDtoList;
    }

    private Ingredients checkIngredientsRepository(Ingredients checkIngredients){
        return Optional.ofNullable(ingredientsRepository.findByNameAndOrigin(checkIngredients.getName(), checkIngredients.getOrigin()))
                .orElseGet(() -> ingredientsRepository.save(checkIngredients));
    }

    private Ingredients ingredientsBuilder (BreadDto.BreadIngredient ingredientsDto) {
        return Ingredients.builder().
                name(ingredientsDto.getName()).
                origin(ingredientsDto.getOrigin()).
                build();
    }
}
