package com.dev.olivebakery.service;

import com.dev.olivebakery.domain.entity.Bread;
import com.dev.olivebakery.domain.entity.Member;
import com.dev.olivebakery.domain.entity.Reservation;
import com.dev.olivebakery.domain.entity.ReservationInfo;
import com.dev.olivebakery.domain.enums.ReservationType;
import com.dev.olivebakery.exception.UserDefineException;
import com.dev.olivebakery.repository.ReservationInfoRepository;
import com.dev.olivebakery.repository.ReservationRepository;
import com.dev.olivebakery.utill.Explain;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import static com.dev.olivebakery.domain.dto.ReservationDto.*;

/**
 * Created by YoungMan on 2019-02-09.
 */

@RequiredArgsConstructor
@Service
public class ReservationService {

	private final ReservationRepository reservationRepository;
	private final ReservationInfoRepository reservationInfoRepository;
	private final SignService signService;
	private final BreadService breadService;
	private final SalesService salesService;

	public Reservation findById(Long reservationId) {
		return reservationRepository.findById(reservationId).orElseThrow(() -> new UserDefineException("해당 예약내역이 없습니다."));
	}

	@Explain("예약 정보 저장")
	public void saveReservation(ReservationSaveRequest saveDto) {
		timeValidationCheck(saveDto.getBringTime());
		reservationInfoRepository.saveAll(convertSaveDtoToEntity(saveDto));
	}

	@Explain("saveReservation 의 서브함수")
	private List<ReservationInfo> convertSaveDtoToEntity(ReservationSaveRequest saveDto) {

		List<ReservationInfo> reservationInfos = new ArrayList<>();
		List<Bread> breads = breadService.findsByNames(saveDto.getBreadNames());
		Member member = signService.findById(saveDto.getUserEmail());
		int finalPrice = breadService.getFinalPrice(saveDto.getBreadNames(), saveDto.getBreadCounts());

		Reservation reservation = Reservation.builder()
				.bringTime(saveDto.getBringTime())
				.member(member)
				.price(finalPrice)
				.build();

		for (int i = 0; i < breads.size(); i++) {
			reservationInfos.add(ReservationInfo.builder()
					.breadCount(saveDto.getBreadCounts().get(i))
					.bread(breads.get(i))
					.reservation(reservation)
					.build()
			);
		}
		return reservationInfos;
	}

	@Explain("예약 정보 수정")
	public void updateReservation(ReservationUpdateRequest reservationUpdateRequest) {
		deleteReservation(reservationUpdateRequest.getReservationId());
		saveReservation(reservationUpdateRequest.getReservationSaveRequest());
	}

	@Explain("예약 정보 삭제")
	public void deleteReservation(Long reservationId) {
		reservationRepository.deleteById(reservationId);
	}

	@Explain("예약 상태 수정")
	public void updateReservationType(Long reservationId) {
		Reservation reservation = findById(reservationId);
		reservation.updateReservationType();
		reservationRepository.save(reservation);
	}

	@Explain("유저의 모든 예약내역을 예약타입별로 가져옴 ")
	public List<ReservationResponse> getReservationInfos(String email, ReservationType reservationType) {
		List<ReservationResponseTemp> reservationResponseTemps = reservationRepository.getReservationInfos(email, reservationType);
		return convertGetTempDtoListToGetDtoList(reservationResponseTemps);
	}

	@Explain("유저의 가장 최근 예약내역을 예약타입에 무관하게 조회")
	public ReservationResponse getReservationInfoByRecently(String email) {
		List<ReservationResponseTemp> reservationResponseTemps = reservationRepository.getReservationInfoByRecently(email);
		return convertGetTmpDtoToGetDto(reservationResponseTemps);
	}

	@Explain("날짜별 예약 조회, Admin 에서 사용")
	public List<ReservationResponse> getReservationInfosByDate(ReservationDateRequest reservationDateRequest) {
		LocalDateTime startDate = reservationDateRequest.getSelectDate().atStartOfDay();
		LocalDateTime endDate = reservationDateRequest.getSelectDate().atTime(23, 59, 59);

		List<ReservationResponseTemp> reservationResponseTemps = reservationRepository.getReservationInfosByDate(reservationDateRequest.getReservationType(), startDate, endDate);
		return convertGetTempDtoListToGetDtoList(reservationResponseTemps);
	}

	@Explain("날짜구간별 예약 조회, Admin 에서 사용")
	public List<ReservationResponse> getReservationInfosByDateRange(ReservationDateRangeRequest reservationDateRangeRequest) {
		LocalDateTime startDate = reservationDateRangeRequest.getStartDate().atStartOfDay();
		LocalDateTime endDate = reservationDateRangeRequest.getEndDate().atTime(23, 59, 59);

		List<ReservationResponseTemp> reservationResponseTemps = reservationRepository.getReservationInfosByDate(reservationDateRangeRequest.getReservationType(), startDate, endDate);
		return convertGetTempDtoListToGetDtoList(reservationResponseTemps);
	}

	@Explain("수령시간은 매일 아침 8시 ~ 저녁 8시 사이// 예약시간보다 늦을 수는 없다")
	public void timeValidationCheck(LocalDateTime bringTime) {
		Predicate<LocalDateTime> predicate = b -> b.isAfter(LocalDateTime.now()) && b.getHour() > 8 && b.getHour() < 20;
		if (!predicate.test(bringTime)) {
			throw new UserDefineException(bringTime.toString() + "  수령시간이 올바르지 않습니다.");
		}
	}

	@Explain("날짜별 예약횟수, 예약 매출 조회 후 저장")
	@Scheduled(cron = "0 0 23 * * MON-FRI")
	public void saveReservationSaleByDate() {
		LocalDateTime startDate = LocalDate.now().atStartOfDay();
		LocalDateTime endDate = LocalDate.now().atTime(23, 59, 59);
		ReservationSale reservationSale = reservationRepository.getReservationSaleByDate(ReservationType.COMPLETE, startDate, endDate);

		if (ObjectUtils.isEmpty(reservationSale)) {
			throw new UserDefineException("예약 내역이 없습니다");
		}
		salesService.saveReservationSale(reservationSale);
	}

	@Explain("ReservationResponseTemp 를 ReservationResponse 로 변환")
	private ReservationResponse convertGetTmpDtoToGetDto(List<ReservationResponseTemp> reservationResponseTemps) {

		List<ReservationBread> reservationBreads = new ArrayList<>();

		for (ReservationResponseTemp reservationResponseTemp : reservationResponseTemps) {
			reservationBreads.add(ReservationBread.of(reservationResponseTemp));
		}
		return ReservationResponse.of(reservationResponseTemps.get(0), reservationBreads);
	}

	@Explain("GetTempDto List 를 GetDto List 로 변환")
	public List<ReservationResponse> convertGetTempDtoListToGetDtoList(List<ReservationResponseTemp> reservationResponseTemps) {

		List<ReservationResponse> reservationRespons = new ArrayList<>();
		List<ReservationBread> reservationBreads = new ArrayList<>();
		Long reservationId = reservationResponseTemps.get(0).getReservationId();

		for (ReservationResponseTemp reservationResponseTemp : reservationResponseTemps) {
			if (reservationResponseTemp.getReservationId().equals(reservationId)) {
				reservationBreads.add(ReservationBread.of(reservationResponseTemp));

				if (reservationResponseTemps.indexOf(reservationResponseTemp) == reservationResponseTemps.size() - 1) {
					reservationRespons.add(ReservationResponse.of(reservationResponseTemps.get(reservationResponseTemps.indexOf(reservationResponseTemp)), reservationBreads));
				}
				continue;
			}
			reservationRespons.add(ReservationResponse.of(reservationResponseTemps.get(reservationResponseTemps.indexOf(reservationResponseTemp) - 1), reservationBreads));

			reservationId = reservationResponseTemp.getReservationId();
			reservationBreads = new ArrayList<>();
			reservationBreads.add(ReservationBread.of(reservationResponseTemp));

			if (reservationResponseTemps.indexOf(reservationResponseTemp) == reservationResponseTemps.size() - 1) {
				reservationRespons.add(ReservationResponse.of(reservationResponseTemps.get(reservationResponseTemps.indexOf(reservationResponseTemp)), reservationBreads));
			}
		}
		return reservationRespons;
	}
}