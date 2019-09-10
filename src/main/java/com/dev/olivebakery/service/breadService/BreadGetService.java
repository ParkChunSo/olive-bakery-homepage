package com.dev.olivebakery.service.breadService;

import com.dev.olivebakery.domain.dtos.BreadDto;
import com.dev.olivebakery.domain.dtos.bread.BreadListResponseDto;
import com.dev.olivebakery.domain.entity.Bread;
import com.dev.olivebakery.domain.entity.BreadImage;
import com.dev.olivebakery.domain.entity.Days;
import com.dev.olivebakery.domain.entity.Ingredients;
import com.dev.olivebakery.domain.enums.DayType;
import com.dev.olivebakery.exception.UserDefineException;
import com.dev.olivebakery.repository.BreadImageRepository;
import com.dev.olivebakery.repository.BreadRepository;
import com.dev.olivebakery.repository.DaysRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BreadGetService {

    private final BreadRepository breadRepository;
    private final DaysRepository daysRepository;
    private final BreadImageRepository breadImageRepository;

    private static final Logger logger = LoggerFactory.getLogger(BreadGetService.class);

    public List<BreadDto.BreadGetAll> getAllBread(){

        List<Bread> breads = breadRepository.findAllByDeleteFlagIsFalse();

        if(breads.size() <= 0){
            throw new UserDefineException("등록 된 빵이 없습니다 !!!");
        }

        return breads2BreadGetAll(breads);
    }

    // 요일 별 빵 가져오기
    public List<BreadDto.BreadGetAll> getBreadByDay(String selectedDay){

        List<Days> days = daysRepository.findByDayType(DayType.valueOf(selectedDay.toUpperCase()));

        List<Bread> breads = new ArrayList<>();

        days.forEach(day -> {
            if(!day.getBread().getDeleteFlag()){
                breads.add(day.getBread());
            }
        });

        if( breads.size()  <= 0 ){
            throw new UserDefineException("해당 요일의 빵이 존재하지 않습니다.");
        }

        return breads2BreadGetAll(breads);
    }

    // bread 엔티티 -> breadGetAll Dto
    public List<BreadDto.BreadGetAll> breads2BreadGetAll(List<Bread> breads) {
        List<BreadDto.BreadGetAll> breadGetAll = new ArrayList<>();

        breads.forEach(bread -> {
            try {
                breadGetAll.add(
                        BreadDto.BreadGetAll.builder()
                                .name(bread.getName())
                                .price(bread.getPrice())
                                .description(bread.getDescription())
                                .isSoldOut(bread.getIsSoldOut())
                                .breadState(bread.getState())
                                .breadImage(getImageDto(bread))
                                .breadIngredientList(ingredientList2Dto(bread.getIngredientsList()))
                                .build());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        return breadGetAll;
    }

    private BreadDto.BreadImageDto getImageDto(Bread bread) throws IOException{
        BreadImage breadImage = breadImageRepository.findByBread(bread).get();

        //image2URL(breadImage.getImageUrl());

        return BreadDto.BreadImageDto.builder()
                 .name(breadImage.getImageName())
                .contentType(breadImage.getImageType())
                .volume(breadImage.getImageSize())
                .imageUrl(breadImage.getImageUrl())
                .build();
    }

    // 브레드 -> 브레드 디테일 엔티티
    public BreadDto.BreadGetDetail getBreadDetails(String name){
        Bread bread = breadRepository.findByName(name)
                .orElseThrow(() -> new UserDefineException(name + "이란 빵은 존재하지 않습니다."));

        List<BreadDto.BreadIngredient> breadIngredientList = ingredientList2Dto(bread.getIngredientsList());
        List<DayType> dayTypes = daysRepository.findByBread(bread);

        return BreadDto.BreadGetDetail.builder()
                .name(bread.getName())
                .price(bread.getPrice())
                .description(bread.getDescription())
                .detailDescription(bread.getDetailDescription())
                .ingredientsList(breadIngredientList)
                .isSoldOut(bread.getIsSoldOut())
                .breadState(bread.getState())
                .daysList(dayTypes)
                .build();
    }

    // 성분 -> 성분 dtos
    private List<BreadDto.BreadIngredient> ingredientList2Dto(List<Ingredients> ingredientsList) {

        List<BreadDto.BreadIngredient> ingredientDtoList = new ArrayList<>();

        ingredientsList.forEach(ingredient -> ingredientDtoList.add(
                BreadDto.BreadIngredient.builder()
                        .name(ingredient.getName())
                        .origin(ingredient.getOrigin())
                        .build()
        ));

        return ingredientDtoList;
    }

    public byte[] getImageResource(String image) throws IOException {

        BreadImage breadImage = breadImageRepository.findByBread(breadRepository.findByName(image).get()).get();
        byte[] result = null;
        try {
            File file = new File(breadImage.getImagePath());

            InputStream in = new FileInputStream(file);

            result = IOUtils.toByteArray(in);

            return result;
        } catch (IOException e){
            logger.error(e.getMessage());
            return null;
        }
    }

    /**
     * 오늘의 빵 가져오기.
     */
    public List<BreadListResponseDto> getTodayBreadList() {
        DayType[] weekDay = { DayType.SUN, DayType.MON, DayType.THU, DayType.WED
                , DayType.THU, DayType.FRI, DayType.SAT};
        Calendar cal = Calendar.getInstance();
        int num = cal.get(Calendar.DAY_OF_WEEK)-1;

        return breadRepository.getBreadListByDay(weekDay[num]);
    }

    /**
     * 특정 요일의 빵 가져오기
     */
    public List<BreadListResponseDto> getBreadListByDay(String day) {
        return breadRepository.getBreadListByDay(DayType.valueOf(day));
    }
}
