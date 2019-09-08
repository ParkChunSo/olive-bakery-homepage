package com.dev.olivebakery.service.reservationService;

import com.dev.olivebakery.domain.dtos.reservation.ReservationBreadDto;
import com.dev.olivebakery.domain.dtos.reservation.ReservationInfoListResponseDto;
import com.dev.olivebakery.domain.dtos.reservation.ReservationInfoListTmpDto;
import com.dev.olivebakery.exception.UserDefineException;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by YoungMan on 2019-05-21.
 */

@Service
public class ReservationConverterService {

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
