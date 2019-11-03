package com.dev.olivebakery.service.reservationService;

import com.dev.olivebakery.domain.dtos.reservation.ReservationSaleTmpDto;
import com.dev.olivebakery.domain.enums.ReservationType;
import com.dev.olivebakery.exception.UserDefineException;
import com.dev.olivebakery.repository.ReservationRepository;
import com.dev.olivebakery.service.SalesService;
import com.dev.olivebakery.utill.DateUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

/**
 * Created by YoungMan on 2019-05-21.
 */

@Service
@RequiredArgsConstructor
public class ReservationSchedulerService {

	private final ReservationRepository reservationRepository;
	private final SalesService salesService;


	/**
	 * 온라인 매출 정보 날짜별 저장
	 */
	@Scheduled(cron = "0 0 23 * * MON-FRI")
	public void saveReservationSaleByDate() {
		ReservationSaleTmpDto reservationSale = reservationRepository.getReservationSaleByDate(
				DateUtils.getStartOfToday(),
				DateUtils.getEndOfToday(),
				ReservationType.COMPLETE
		);

		if (ObjectUtils.isEmpty(reservationSale)) {
			throw new UserDefineException("예약 내역이 없습니다");
		}
		salesService.saveReservationSale(reservationSale);
	}

}
