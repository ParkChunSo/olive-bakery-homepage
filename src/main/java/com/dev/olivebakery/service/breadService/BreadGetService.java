package com.dev.olivebakery.service.breadService;

import com.dev.olivebakery.domain.dto.BreadDto;
import com.dev.olivebakery.domain.entity.Bread;
import com.dev.olivebakery.domain.entity.BreadImage;
import com.dev.olivebakery.domain.entity.Days;
import com.dev.olivebakery.domain.entity.Ingredients;
import com.dev.olivebakery.domain.enums.DayType;
import com.dev.olivebakery.exception.UserDefineException;
import com.dev.olivebakery.repository.BreadImageRepository;
import com.dev.olivebakery.repository.BreadRepository;
import com.dev.olivebakery.repository.DaysRepository;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

@Service
public class BreadGetService {

    private BreadRepository breadRepository;
    private DaysRepository daysRepository;
    private BreadImageRepository breadImageRepository;

    private static final Logger logger = LoggerFactory.getLogger(BreadGetService.class);
    private static final String IMAGE_PATH = "C:\\Users\\Kimyunsang\\Desktop\\spring\\imageTest\\";


    public BreadGetService(BreadRepository breadRepository, DaysRepository daysRepository, BreadImageRepository breadImageRepository){
        this.breadRepository = breadRepository;
        this.daysRepository = daysRepository;
        this.breadImageRepository = breadImageRepository;
    }

    public List<BreadDto.BreadGetAll> getAllBread(){

        List<Bread> breads = breadRepository.findAll();

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

        return breads2BreadGetAll(breads);
    }

    // bread 엔티티 -> breadGetAll Dto
    private List<BreadDto.BreadGetAll> breads2BreadGetAll(List<Bread> breads) {
        List<BreadDto.BreadGetAll> breadGetAlls = new ArrayList<>();

        breads.forEach(bread -> {
            try {
                breadGetAlls.add(
                        BreadDto.BreadGetAll.builder()
                                .name(bread.getName())
                                .price(bread.getPrice())
                                .description(bread.getDescription())
                                .isSoldOut(bread.getIsSoldOut())
                                .breadState(bread.getState())
                                .breadImage(getImageDto(bread))
                                .build());

            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        return breadGetAlls;
    }

    private void image2URL(String filePath) throws IOException {
        URL url = new URL(filePath);
        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
        httpURLConnection.setRequestMethod("HEAD");

        logger.info("url == " + url);
    }

    private String image2Base64(String filePath) throws IOException {
        byte[] fileContent = FileUtils.readFileToByteArray(new File(filePath));
        String encodedString = Base64.getEncoder().encodeToString(fileContent);

        return encodedString;
    }

    private BreadDto.BreadImageDto getImageDto(Bread bread) throws IOException{
        BreadImage breadImage = breadImageRepository.findByBread(bread).get();

        //image2URL(breadImage.getImageUrl());

        return BreadDto.BreadImageDto.builder()
                 .name(breadImage.getImageName())
                .contentType(breadImage.getImageType())
                .volume(breadImage.getImageSize())
                .imageUrl(breadImage.getImageUrl())
                .encoded(image2Base64(breadImage.getImagePath()))
                .build();
    }

    // 브레드 -> 브레드 디테일 엔티티
    public BreadDto.BreadGetDetail getBreadDetails(String name){
        Bread bread = breadRepository.findByName(name)
                .orElseThrow(() -> new UserDefineException(name + "이란 빵은 존재하지 않습니다."));

        List<BreadDto.BreadIngredient> breadIngredientList = ingredientList2Dto(bread.getIngredients());

        return BreadDto.BreadGetDetail.builder()
                .name(bread.getName())
                .price(bread.getPrice())
                .detailDescription(bread.getDetailDescription())
                .ingredientsList(breadIngredientList)
                .isSoldOut(bread.getIsSoldOut())
                .breadState(bread.getState())
                .build();
    }

    // 성분 -> 성분 dto
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

        byte[] result = null;
        try {
            File file = new File(IMAGE_PATH + image + ".jpg");

            InputStream in = new FileInputStream(file);

            result = IOUtils.toByteArray(in);

            return result;
        } catch (IOException e){
            logger.error(e.getMessage());
            return result;
        }
    }
}
