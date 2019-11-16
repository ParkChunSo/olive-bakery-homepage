package com.dev.olivebakery.service.breadService;

import com.dev.olivebakery.domain.dtos.bread.BreadRequestDto;
import com.dev.olivebakery.domain.entity.Bread;
import com.dev.olivebakery.domain.entity.BreadImage;
import com.dev.olivebakery.domain.entity.Days;
import com.dev.olivebakery.domain.entity.Ingredients;
import com.dev.olivebakery.domain.enums.BreadState;
import com.dev.olivebakery.exception.UserDefineException;
import com.dev.olivebakery.repository.BreadRepository;
import com.dev.olivebakery.repository.DaysRepository;
import com.dev.olivebakery.repository.IngredientsRepository;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;


@Service
@Log
@RequiredArgsConstructor
public class BreadSaveService {

  private static final String IMAGE_PATH_KEY = "resources.image-locations";
  @Autowired
  private Environment environment;

  private final BreadRepository breadRepository;
  private final IngredientsRepository ingredientsRepository;
  private final DaysRepository daysRepository;

  /**
   * 빵 저장하기
   */
  public void saveBread(BreadRequestDto breadRequestDto, MultipartFile file) {
    if (ObjectUtils.isEmpty(breadRequestDto)) {
      throw new UserDefineException("잘못된 형식의 요청입니다.");
    }

    Bread bread = breadRequestDto.toEntity();
    breadRepository.save(bread);

    bread = breadRepository.findByName(breadRequestDto.getName())
        .orElseThrow(() -> new UserDefineException("빵을 저장하는데 오류가 발생했습니다."));
    bread.updateBreadIngredients(
        Ingredients.newListInstance(bread, breadRequestDto.getIngredientsList()));
    bread.updateDays(Days.newListInstance(bread, breadRequestDto.getDays()));
    List<BreadImage> images = new ArrayList<>();
    try {
      images.add(BreadImage.of(file, bread, environment.getProperty(IMAGE_PATH_KEY)));
      bread.updateBreadImages(images);
    } catch (IOException e) {
//            throw new UserDefineException("이미지 저장하는데 오류가 발생했습니다.", e.getMessage());
      System.out.println(e.getMessage());
    }
    breadRepository.save(bread);
  }

  /**
   * 빵 전체 정보 변경하기
   */
  public void updateBread(BreadRequestDto breadRequestDto, MultipartFile image) throws IOException {
    Bread bread = breadRepository.findByName(breadRequestDto.getName())
        .orElseThrow(() -> new UserDefineException(breadRequestDto.getName() + "이란 빵은 존재하지 않습니다."));

    daysRepository.deleteByBread(bread);
    ingredientsRepository.deleteByBread(bread);

    if (!ObjectUtils.isEmpty(image)) {
      bread.addBreadImages(BreadImage.of(image, bread, environment.getProperty(IMAGE_PATH_KEY)));
    }

    bread.updateBreadInfo(breadRequestDto.getPrice(), breadRequestDto.getDescription(),
        breadRequestDto.getDetailDescription());

    bread.updateBreadIngredients(
        Ingredients.newListInstance(bread, breadRequestDto.getIngredientsList())
    );
    bread.updateDays(
        Days.newListInstance(bread, breadRequestDto.getDays())
    );

    breadRepository.save(bread);
  }

  /**
   * 빵 상태 수정하기
   */
  public void updateBreadState(Long id, BreadState state) {
    Bread bread = breadRepository.findById(id)
        .orElseThrow(() -> new UserDefineException("해당 빵이 존재하지 않습니다."));
    bread.updateBreadState(state);

    breadRepository.save(bread);
  }

  /**
   * 빵 매진 정보 수정하기
   */
  public void updateBreadSoldOut(Long id, boolean isSoldOut) {
    Bread bread = breadRepository.findById(id)
        .orElseThrow(() -> new UserDefineException("해당 빵이 존재하지 않습니다."));
    bread.updateBreadSoldOut(isSoldOut);

    breadRepository.save(bread);
  }

  public void deleteBread(Long id, boolean delete) {
    Bread bread = breadRepository.findById(id)
        .orElseThrow(() -> new UserDefineException("해당 빵이 존재하지 않습니다."));
    bread.deleteBread(delete);

    breadRepository.save(bread);
  }
}
