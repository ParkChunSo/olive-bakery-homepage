package com.dev.olivebakery.service.breadService;

import com.dev.olivebakery.domain.daos.BreadListDao;
import com.dev.olivebakery.domain.dtos.bread.BreadListResponseDto;
import com.dev.olivebakery.domain.dtos.bread.IngredientListResponseDto;
import com.dev.olivebakery.domain.enums.DayType;
import com.dev.olivebakery.repository.BreadRepository;
import com.dev.olivebakery.utill.ConverterUtils;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BreadGetService {

    private final BreadRepository breadRepository;

    private static final Logger logger = LoggerFactory.getLogger(BreadGetService.class);

    /**
     * 등록된 모든 빵정보 가져오기
     */
    public List<BreadListResponseDto> getAllBreadList(){
        return ConverterUtils.convertBreadDao2BreadListResponseDto(breadRepository.getAllBreadList());
    }

    /**
     * 오늘의 빵 가져오기.
     */
    public List<BreadListResponseDto> getTodayBreadList() {
        DayType[] weekDay = { DayType.SUN, DayType.MON, DayType.TUE, DayType.WED
                , DayType.THU, DayType.FRI, DayType.SAT};
        Calendar cal = Calendar.getInstance();
        int num = cal.get(Calendar.DAY_OF_WEEK)-1;

        List<BreadListDao> breadListByDay = breadRepository.getBreadListByDay(weekDay[num]);
        if(ObjectUtils.isEmpty(breadListByDay))
            return new ArrayList<>();

        return ConverterUtils.convertBreadDao2BreadListResponseDto(breadListByDay);
    }

    /**
     * 특정 요일의 빵 가져오기
     */
    public List<BreadListResponseDto> getBreadListByDay(String day) {
        return ConverterUtils.convertBreadDao2BreadListResponseDto(breadRepository.getBreadListByDay(DayType.valueOf(day)));
    }

    /**
     * 특정 빵 정보 가져오기
     */
    public BreadListResponseDto getBreadDetail(String breadName){
        List<BreadListDao> daos = breadRepository.getBreadByBreadName(breadName);
        if(daos.isEmpty())
            return new BreadListResponseDto();
        return ConverterUtils.convertBreadDaoList2BreadListResponseDto(daos);
    }

    /**
     * 빵의 이미지 가져오기
     */
    public byte[] getImageResource(String image){
        List<String> breadImages = breadRepository.getImagePathByBreadName(image);
        if(breadImages.isEmpty())
            return null;
        byte[] result;
        try {
            File file = new File(breadImages.get(0));

            InputStream in = new FileInputStream(file);

            result = IOUtils.toByteArray(in);

            return result;
        } catch (IOException e){
            logger.error(e.getMessage());
            return null;
        }
    }

    public List<IngredientListResponseDto> getIngredientList(){
        return breadRepository.getIngredientList();
    }
}
