package com.dev.olivebakery.controller;

import com.dev.olivebakery.domain.dtos.BreadDto;
import com.dev.olivebakery.domain.entity.Bread;
import com.dev.olivebakery.repository.BreadRepository;
import com.dev.olivebakery.repository.IngredientsRepository;
import com.dev.olivebakery.service.breadService.BreadGetService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class BreadControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BreadRepository breadRepository;

    @Autowired
    private BreadGetService breadGetService;

    @Autowired
    private IngredientsRepository ingredientsRepository;

    BreadDto.BreadSave breadDto;

    ObjectMapper mapper = new ObjectMapper();

    @Before
    public void setup() {
//        List<BreadDto.BreadIngredient> breadIngredientList = new ArrayList<>();
//        BreadDto.BreadIngredient ingredient = BreadDto.BreadIngredient.builder().name("밀가루").origin("한국").build();
//
//        breadIngredientList.add(ingredient);
//
//        breadDto = BreadDto.BreadSave.builder()
//                .name("치아바타")
//                .price(10000)
//                .description("맛있는 빵")
//                .detailDescription("자세한 맛있는 빵")
//                .ingredients(breadIngredientList).build();
//
//        Bread bread = Bread.builder().name("치아바타").build();
//        breadRepository.save(bread);
    }

        @Test
        public void test1() {
//        this.mockMvc.perform(MockMvcRequestBuilders
////                .post("/olive/bread/")
////                .content(asJson))
//
//        this.mockMvc.perform(MockMvcRequestBuilders
//            .put("olive/bread/name")
//            .content(mapper.writeValueAsString(breadDto))
//            .contentType(MediaType.APPLICATION_JSON.getType())
//            .andExpect(state().))
    }

    @Test
    public void getBreadAll() {
//        assertThat(breadRepository.findAllByDeleteFlagIsFalse().size()).isEqualTo(3);
        List<Bread> breadList = breadRepository.findAllByDeleteFlagIsFalse();

//        breadGetService.
    }

    @Test
    public void deleteIngredients() {

        Bread bread = breadRepository.findByName("마늘빵").get();

//        ingredientsRepository.deleteAllByBread(bread);
    }

}