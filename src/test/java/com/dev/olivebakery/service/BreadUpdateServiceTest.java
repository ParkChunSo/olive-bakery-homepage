package com.dev.olivebakery.service;


import com.dev.olivebakery.domain.entity.Bread;
import com.dev.olivebakery.domain.entity.Ingredients;
import com.dev.olivebakery.repository.BreadRepository;
import com.dev.olivebakery.repository.IngredientsRepository;
import lombok.extern.java.Log;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
@Log
public class BreadUpdateServiceTest {
//
//    @Autowired
//    IngredientsRepository ingredientsRepository;
//
//    @Autowired
//    BreadRepository breadRepository;
//
//    @Test
//    public void ingredientsUpdateTest(){
//
//        Optional<Bread> bread = breadRepository.findByName("식빵2");
//
//        bread.get().getIngredients().forEach(ingredients -> {
//            log.info("before ---"  + ingredients.getName() + "  " + ingredients.getOrigin());
//        });
//
//        Ingredients newIngredients = ingredientsRepository.save(Ingredients.builder().name("밀가루22").origin("한국").build());
//
//        List<Ingredients> list = new ArrayList<>();
//
//        list.add(newIngredients);
//
//        bread.get().updateBreadIngredients(list);
//
//
////        bread.get().addBreadIngredients(Ingredients.builder().name("밀가루22").origin("한국").build());
//
//        bread.get().getIngredients().forEach(ingredients -> {
//            log.info("after ---"  + ingredients.getName() + "  " + ingredients.getOrigin());
//        });
//
//        Bread save = breadRepository.save(bread.get());
//
//
//        save.getIngredients().forEach(ingredients -> {
//            log.info("save ---"  + ingredients.getName() + "  " + ingredients.getOrigin());
//        });
////
////        Optional<Bread> bread2 = breadRepository.findByName("식빵2");
////
////        bread2.get().getIngredients().forEach(ingredients -> {
////            log.info("after save  ---"  + ingredients.getName() + "  " + ingredients.getOrigin());
////        });
//    }
}