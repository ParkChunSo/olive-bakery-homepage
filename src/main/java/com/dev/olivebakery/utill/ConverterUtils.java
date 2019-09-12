package com.dev.olivebakery.utill;

import com.dev.olivebakery.domain.daos.BreadListDao;
import com.dev.olivebakery.domain.dtos.bread.BreadListResponseDto;
import com.dev.olivebakery.domain.dtos.bread.IngredientListResponseDto;
import com.dev.olivebakery.domain.dtos.reservation.ReservationBreadDto;
import com.dev.olivebakery.domain.dtos.reservation.ReservationInfoListResponseDto;
import com.dev.olivebakery.domain.dtos.reservation.ReservationInfoListTmpDto;
import com.dev.olivebakery.exception.UserDefineException;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;

public class ConverterUtils {

    /**
     * List<BreadListDao> -> List<BreadListResponseDto>
     */
    public static List<BreadListResponseDto> convertBreadDao2BreadListResponseDto(List<BreadListDao> breadList){
        List<BreadListResponseDto> result = new ArrayList<>();
        List<BreadListDao> tmpList = new ArrayList<>();
        Long breadId = breadList.get(0).getBreadId();

        for(BreadListDao dao : breadList){
            if(!breadId.equals(dao.getBreadId())) {
                result.add(convertBreadDaoList2BreadListResponseDto(tmpList));
                tmpList.clear();
                breadId = dao.getBreadId();
            }

            tmpList.add(dao);
        }
        result.add(convertBreadDaoList2BreadListResponseDto(tmpList));

        return result;
    }

    /**
     * List<BreadListDao> -> BreadListResponseDto
     */
    public static BreadListResponseDto convertBreadDaoList2BreadListResponseDto(List<BreadListDao> daos){
        List<String> days = new ArrayList<>();
        List<String> ingredientName = new ArrayList<>();
        List<IngredientListResponseDto> ingredients = new ArrayList<>();

        for(BreadListDao tmp : daos){

            if(!days.contains(tmp.getDays()))
                days.add(tmp.getDays());

            if(!ingredientName.contains(tmp.getIngrediantName())) {
                ingredients.add(
                        IngredientListResponseDto.builder()
                                .name(tmp.getIngrediantName())
                                .origin(tmp.getIngrediantOrigin())
                                .build()
                );
                ingredientName.add(tmp.getIngrediantName());
            }
        }
        BreadListDao tmp = daos.get(0);

        return  BreadListResponseDto.builder()
                .name(tmp.getBreadName())
                .price(tmp.getBreadPrice())
                .description(tmp.getDescription())
                .detailDescription(tmp.getDetailDescription())
                .days(days)
                .isSoldOut(tmp.getIsSoldOut())
                .state(tmp.getState())
                .ingredientsList(ingredients)
                .imageUrl(tmp.getImageUrl())
                .build();

    }

    /**
     * List<ReservationInfoListTmpDto> -> ReservationInfoListResponseDto
     */
    public static ReservationInfoListResponseDto convertGetTmpDtoToGetDto(List<ReservationInfoListTmpDto> reservationResponseTemps) {

        if(ObjectUtils.isEmpty(reservationResponseTemps)) {
            throw new UserDefineException("예약 내역이 없습니다.");
        }
        List<ReservationBreadDto> reservationBreads = new ArrayList<>();

        for (ReservationInfoListTmpDto reservationResponseTemp : reservationResponseTemps) {
            reservationBreads.add(ReservationBreadDto.of(reservationResponseTemp));
        }
        return ReservationInfoListResponseDto.of(reservationResponseTemps.get(0), reservationBreads);
    }

    /**
     * List<ReservationInfoListTmpDto> -> List<ReservationInfoListResponseDto>
     */
    public static List<ReservationInfoListResponseDto> convertGetTempDtoListToGetDtoList(List<ReservationInfoListTmpDto> reservationResponseTemps) {

        List<ReservationInfoListResponseDto> reservationResponses = new ArrayList<>();
        List<ReservationBreadDto> reservationBreads = new ArrayList<>();
        Long reservationId = reservationResponseTemps.get(0).getReservationId();

        for (ReservationInfoListTmpDto reservationResponseTemp : reservationResponseTemps) {
            if (reservationResponseTemp.getReservationId().equals(reservationId)) {
                reservationBreads.add(ReservationBreadDto.of(reservationResponseTemp));

                if (reservationResponseTemps.indexOf(reservationResponseTemp) == reservationResponseTemps.size() - 1) {
                    reservationResponses.add(ReservationInfoListResponseDto.of(reservationResponseTemps.get(reservationResponseTemps.indexOf(reservationResponseTemp)),
                            reservationBreads)
                    );
                }
                continue;
            }
            reservationResponses.add(ReservationInfoListResponseDto.of(reservationResponseTemps.get(reservationResponseTemps.indexOf(reservationResponseTemp) - 1),
                    reservationBreads));

            reservationId = reservationResponseTemp.getReservationId();
            reservationBreads = new ArrayList<>();
            reservationBreads.add(ReservationBreadDto.of(reservationResponseTemp));

            if (reservationResponseTemps.indexOf(reservationResponseTemp) == reservationResponseTemps.size() - 1) {
                reservationResponses.add(ReservationInfoListResponseDto.of(reservationResponseTemps.get(reservationResponseTemps.indexOf(reservationResponseTemp)),
                        reservationBreads)
                );
            }
        }
        return reservationResponses;
    }
}
