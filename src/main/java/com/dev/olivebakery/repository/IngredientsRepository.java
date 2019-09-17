package com.dev.olivebakery.repository;

import com.dev.olivebakery.domain.entity.Bread;
import com.dev.olivebakery.domain.entity.Ingredients;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface IngredientsRepository extends JpaRepository<Ingredients, Long> {
//    @Transactional
//    @Modifying
//    @Query("delete from Ingredients i where i.bread = :bread")
//    void deleteAllByBread(@Param(value = "bread") Bread bread);

//    @Transactional
//    @Modifying
//    @Query("delete from Ingredients i where i.bread = :bread and i.name = :name and i.origin = :origin" )
//    void deleteIngredientsByBread(@Param(value = "bread") Bread bread, @Param(value = "name") String name, @Param(value = "origin") String origin);

    Ingredients findByNameAndOrigin(String name, String origin);
    @Transactional
    void deleteByBread(Bread bread);
    @Override
    List<Ingredients> findAll();
}
