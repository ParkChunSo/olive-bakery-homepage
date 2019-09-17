package com.dev.olivebakery.repository.implement;

import com.dev.olivebakery.domain.daos.BreadListDao;
import com.dev.olivebakery.domain.dtos.bread.IngredientListResponseDto;
import com.dev.olivebakery.domain.entity.*;
import com.dev.olivebakery.domain.enums.DayType;
import com.dev.olivebakery.repository.custom.BreadRepositoryCustom;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;

@Component
public class BreadRepositoryImpl extends QuerydslRepositorySupport implements BreadRepositoryCustom {
    @PersistenceContext
    EntityManager entityManager;
    private QBread bread = QBread.bread;
    private QDays days = QDays.days;
    private QIngredients ingredients = QIngredients.ingredients;
    private QBreadImage breadImage = QBreadImage.breadImage;

    public BreadRepositoryImpl(){
        super(Bread.class);
    }

    @Override
    public List<BreadListDao> getAllBreadList() {
        JPAQuery<BreadListDao> breadQuery = setQuery();
        return breadQuery.fetch();
    }

    @Override
    public List<BreadListDao> getBreadListByDay(DayType day) {
        JPAQuery<BreadListDao> breadQuery = setQuery();
        return breadQuery.where(days.dayType.eq(day))
                        .fetch();
    }

    @Override
    public List<BreadListDao> getBreadByBreadName(String breadName) {
        JPAQuery<BreadListDao> breadQuery = setQuery();
        return breadQuery.where(bread.name.eq(breadName)).fetch();
    }

    @Override
    public List<String> getImagePathByBreadName(String breadName) {
        JPAQueryFactory query = new JPAQueryFactory(entityManager);
        Long breadId = query.select(bread.breadId).from(bread).where(bread.name.eq(breadName)).fetchOne();
        if(breadId == null)
            return new ArrayList<>();
        return query.select(breadImage.imagePath)
                .from(bread)
                .join(bread, breadImage.bread)
                .where(bread.breadId.eq(breadId)).fetch();
    }

    @Override
    public List<IngredientListResponseDto> getIngredientList(){
        JPAQuery<IngredientListResponseDto> ingredientQuery = new JPAQuery<>(entityManager);
        return ingredientQuery.select(Projections.constructor(IngredientListResponseDto.class, ingredients.name, ingredients.origin))
                .from(ingredients)
                .distinct().fetch();
    }

    private JPAQuery<BreadListDao> setQuery(){
        JPAQuery<BreadListDao> breadQuery = new JPAQuery<>(entityManager);
        return breadQuery.select(Projections.constructor(BreadListDao.class, bread.breadId, bread.name, bread.price, bread.description
                , bread.detailDescription, days.dayType, bread.isSoldOut, bread.state, ingredients.name, ingredients.origin, breadImage.imageUrl))
                .from(bread)
                .leftJoin(bread.ingredients, ingredients)
                .leftJoin(bread.days, days)
                .leftJoin(bread.images, breadImage)
                .where(bread.isDeleted.eq(true));
    }
}
