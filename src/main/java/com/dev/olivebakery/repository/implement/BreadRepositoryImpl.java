package com.dev.olivebakery.repository.implement;

import com.dev.olivebakery.domain.dtos.bread.BreadListResponseDto;
import com.dev.olivebakery.domain.dtos.bread.IngredientListResponseDto;
import com.dev.olivebakery.domain.dtos.bread.SoldOutTmpDto;
import com.dev.olivebakery.domain.entity.*;
import com.dev.olivebakery.domain.enums.DayType;
import com.dev.olivebakery.repository.custom.BreadRepositoryCustom;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

public class BreadRepositoryImpl extends QuerydslRepositorySupport implements BreadRepositoryCustom {
    @PersistenceContext
    EntityManager entityManager;
    private QBread bread = QBread.bread;
    private QDays days = QDays.days;
    private QIngredients ingredients = QIngredients.ingredients;
    private QBreadImage breadImage = QBreadImage.breadImage;
    private QSoldOut soldOut = QSoldOut.soldOut;

    public BreadRepositoryImpl(){
        super(Bread.class);
    }

    @Override
    public List<BreadListResponseDto> getBreadListByDay(DayType day) {
        JPAQuery<BreadListResponseDto> breadQuery = new JPAQuery<>(entityManager);
        List<BreadListResponseDto> breadList = breadQuery.select(Projections.constructor(BreadListResponseDto.class, bread.name, bread.price, bread.description
                                                                , bread.detailDescription, bread.state, breadImage.imageUrl))
                                                    .from(bread)
                                                    .join(bread, days.bread)
                                                    .where(days.dayType.eq(day)).fetch();

        JPAQuery<IngredientListResponseDto> ingredientQuery = new JPAQuery<>(entityManager);
        ingredientQuery.select(ingredients.name, ingredients.origin)
                .from(ingredients);
        for(int i = 0 ; i < breadList.size() ; i++){
            breadList.get(i).setIngredientsList(
                ingredientQuery
                        .where(ingredients.bread.name.eq(breadList.get(i).getName()))
                        .fetch()
            );
        }

        JPAQuery<SoldOutTmpDto> soldOutQuery = new JPAQuery<>(entityManager);
        return null;
    }
}
