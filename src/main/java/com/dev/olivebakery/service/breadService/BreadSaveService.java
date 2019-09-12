package com.dev.olivebakery.service.breadService;

import com.dev.olivebakery.domain.dtos.BreadDto;
import com.dev.olivebakery.domain.entity.Bread;
import com.dev.olivebakery.domain.entity.BreadImage;
import com.dev.olivebakery.domain.entity.Days;
import com.dev.olivebakery.domain.entity.Ingredients;
import com.dev.olivebakery.domain.enums.BreadState;
import com.dev.olivebakery.domain.enums.DayType;
import com.dev.olivebakery.repository.BreadImageRepository;
import com.dev.olivebakery.repository.BreadRepository;
import com.dev.olivebakery.repository.DaysRepository;
import com.dev.olivebakery.repository.IngredientsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.*;


@Service
@Log
@RequiredArgsConstructor
public class BreadSaveService {

    //private static final String IMAGE_PATH = "C:\\Users\\Kimyunsang\\Desktop\\spring\\imageTest\\";

    private static final String IMAGE_PATH_KEY = "resources.image-locations";
    @Autowired
    private Environment environment;

    private final BreadRepository breadRepository;
    private final IngredientsRepository ingredientsRepository;
    private final DaysRepository daysRepository;
    private final BreadImageRepository breadImageRepository;

    public Bread saveBread(BreadDto.BreadSave breadSave, MultipartFile image) throws IOException{

//        breadRepository.findByName(breadSave.getName())
//                .ifPresent(bread -> {
//                    log.info("bread ---- 존재" + bread.getName());
//                    throw new UserDefineException("해당 이름의 빵이 이미 존재합니다.");
//                });

//        log.info("check bread name  " + checkBreadName(breadSave.getName()));

        log.info("bread save ------------");

        Bread bread = breadSaveDto2Bread(breadSave);

        breadRepository.save(bread);

        saveDays(breadSave.getDayTypes(), bread);

        BreadImage breadImage = saveImage(image, bread);

        breadImageRepository.save(breadImage);

        return bread;
    }

    public Boolean checkBreadName(String breadName){
        breadRepository.findByName(breadName)
                .ifPresent(bread -> {
                    return;
                });
        return false;
    }

    private Bread breadSaveDto2Bread(BreadDto.BreadSave breadSave){
        return Bread.builder()
                .name(breadSave.getName())
                .price(breadSave.getPrice())
                .description(breadSave.getDescription())
                .detailDescription(breadSave.getDetailDescription())
                .ingredients(getIngredientsListFromIngredientsDtoList(breadSave.getIngredientsList()))
                .state(BreadState.NEW)
                .isSoldOut(false)
                .isDeleted(false)
                .build();
    }

    public List<Ingredients> getIngredientsListFromIngredientsDtoList(List<BreadDto.BreadIngredient> breadIngredientsList) {
        List<Ingredients> ingredientsList = new ArrayList<>();
        breadIngredientsList.forEach(breadIngredients -> {
            Ingredients ingredients = Optional.ofNullable(ingredientsRepository.findByNameAndOrigin(breadIngredients.getName(), breadIngredients.getOrigin()))
                    .orElseGet(() -> ingredientsRepository.save(Ingredients.builder().name(breadIngredients.getName()).origin(breadIngredients.getOrigin()).build()));
            ingredientsList.add(ingredients);
        });

        return ingredientsList;
    }

    public void saveDays(List<DayType> daysTypes, Bread bread){

        daysTypes.forEach(dayType -> {
            Days days = Days.builder()
                    .bread(bread)
                    .dayType(dayType).build();

            daysRepository.save(days);
        });
    }

    public BreadImage saveImage(MultipartFile imageFile, Bread bread) throws IOException {

        UUID uid = UUID.randomUUID(); // 유니크 값 생성

        String fileName = uid + "_" + imageFile.getOriginalFilename();

        String savePath = calcPath(environment.getProperty(IMAGE_PATH_KEY));

        File destinationFile = new File(environment.getProperty(IMAGE_PATH_KEY) + savePath, fileName);

        imageFile.transferTo(destinationFile);

        String imageUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/olive/bread/image/" + bread.getName())
                .toUriString();

        return BreadImage.builder()
                .imageName(imageFile.getOriginalFilename())
                .imageSize(imageFile.getSize())
                .imageType(imageFile.getContentType())
                .imageUrl(imageUrl)
                .imagePath(environment.getProperty(IMAGE_PATH_KEY) + savePath + File.separator + fileName)
                .current(true)
                .bread(bread)
                .build();
    }

    // 폴더 생성 함수
    @SuppressWarnings("unused")
    private static String calcPath(String uploadPath) {

        Calendar cal = Calendar.getInstance();

        String yearPath = File.separator + cal.get(Calendar.YEAR); // 연도 별 폴더 경로

        String monthPath = yearPath + File.separator + new DecimalFormat("00").format(cal.get(Calendar.MONTH) + 1); // 월 별 폴더 경로

        String datePath = monthPath + File.separator + new DecimalFormat("00").format(cal.get(Calendar.DATE)); // 일 별 폴더 경로

        makeDir(uploadPath, yearPath, monthPath, datePath); // 폴더 생성

        return datePath;
    }

    // 폴더 생성 함수
    private static void makeDir(String uploadPath, String... paths) {

        if (new File(uploadPath + paths[paths.length - 1]).exists()) {
            return;
        } // 해당 경로의 폴더가 존재하면 반환

        for (String path : paths) {
            File dirPath = new File(uploadPath + path);

            if (!dirPath.exists()) {
                dirPath.mkdir();
            } // 해당 경로의 폴더 생성
        }
    }
}
