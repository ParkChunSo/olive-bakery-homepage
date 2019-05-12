package com.dev.olivebakery.service.breadService;

import com.dev.olivebakery.domain.dto.BreadDto;
import com.dev.olivebakery.domain.entity.Bread;
import com.dev.olivebakery.domain.entity.BreadImage;
import com.dev.olivebakery.domain.entity.Days;
import com.dev.olivebakery.domain.entity.Ingredients;
import com.dev.olivebakery.domain.enums.DayType;
import com.dev.olivebakery.repository.BreadImageRepository;
import com.dev.olivebakery.repository.BreadRepository;
import com.dev.olivebakery.repository.DaysRepository;
import com.dev.olivebakery.repository.IngredientsRepository;
import lombok.extern.java.Log;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;


@Service
@Log
public class BreadSaveService {

    private static final String IMAGE_PATH = "C:\\Users\\Kimyunsang\\Desktop\\spring\\imageTest\\";

    private final BreadRepository breadRepository;
    private final IngredientsRepository ingredientsRepository;
    private final DaysRepository daysRepository;
    private final BreadImageRepository breadImageRepository;




    public BreadSaveService(BreadRepository breadRepository, IngredientsRepository ingredientsRepository, DaysRepository daysRepository,
                            BreadImageRepository breadImageRepository) {
        this.breadRepository = breadRepository;
        this.ingredientsRepository = ingredientsRepository;
        this.daysRepository = daysRepository;
        this.breadImageRepository = breadImageRepository;
    }

    public void saveBread(BreadDto.BreadSave breadSave, MultipartFile image) throws IOException{

        log.info(breadSave.getName());

        Bread bread = breadSaveDto2Bread(breadSave);

        breadRepository.save(bread);

        saveIngredients(breadSave.getIngredientsList(), bread);
        saveDays(breadSave.getDayTypes(), bread);

        saveImage(image, bread);

    }

    private Bread breadSaveDto2Bread(BreadDto.BreadSave breadSave){
        return Bread.builder()
                .name(breadSave.getName())
                .price(breadSave.getPrice())
                .description(breadSave.getDescription())
                .detailDescription(breadSave.getDetailDescription())
                .isSoldOut(false)
                .deleteFlag(false)
                .build();
    }

    public void saveIngredients(List<BreadDto.BreadIngredient> breadIngredients, Bread bread) {
        breadIngredients.forEach(breadIngredient -> {
            Ingredients ingredients = Ingredients.builder()
                    .bread(bread)
                    .name(breadIngredient.getName())
                    .origin(breadIngredient.getOrigin())
                    .build();

            ingredientsRepository.save(ingredients);
        });
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

        String fileName = imageFile.getOriginalFilename();
        File destinationFile = new File(IMAGE_PATH+ File.separator + fileName);

        imageFile.transferTo(destinationFile);

        String imageUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/olive/bread/image/" + fileName)
                .toUriString();

        BreadImage breadImage = BreadImage.builder()
                .imageName(imageFile.getOriginalFilename())
                .imageSize(imageFile.getSize())
                .imageType(imageFile.getContentType())
                .imageUrl(imageUrl)
                .imagePath(IMAGE_PATH + fileName)
                .current(true)
                .bread(bread)
                .build();

        return breadImageRepository.save(breadImage);
    }
}
