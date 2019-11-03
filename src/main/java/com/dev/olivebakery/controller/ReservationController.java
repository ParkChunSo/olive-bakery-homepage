package com.dev.olivebakery.controller;

import com.dev.olivebakery.domain.dtos.reservation.*;
import com.dev.olivebakery.domain.enums.ReservationType;
import com.dev.olivebakery.service.reservationService.ReservationDeleteService;
import com.dev.olivebakery.service.reservationService.ReservationGetService;
import com.dev.olivebakery.service.reservationService.ReservationSaveService;
import com.dev.olivebakery.service.reservationService.ReservationUpdateService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(value = "/olive/reservation")
@RequiredArgsConstructor
public class ReservationController {

	private final ReservationGetService reservationGetService;
	private final ReservationSaveService reservationSaveService;
	private final ReservationUpdateService reservationUpdateService;
	private final ReservationDeleteService reservationDeleteService;


	@ApiOperation(value = "유저의 전체 에약 내역 조회", notes = "유저가 예약한 전체 내역 조회")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "type", value = "예약타입", required = true),
	})
	@GetMapping("/user/type/{type}")
	public List<ReservationInfoListResponseDto> getReservationInfos(@PathVariable("type") ReservationType reservationType,
																	@RequestHeader(name = "Authorization") String bearerToken) {

		return reservationGetService.getReservationInfos(reservationType, bearerToken);
	}


	@ApiOperation(value = "유저의 가장 최근 예약 내역 조회", notes = "유저의 가장 최근 예약내역을 예약타입에 무관하게 조회")
	@GetMapping("/user/recently")
	public ReservationInfoListResponseDto getReservationInfosByDate(@RequestHeader(name = "Authorization") String bearerToken) {
		return reservationGetService.getReservationInfoByRecently(bearerToken);
	}

	@ApiOperation(value = "유저의 예약내역 조회(관리자)", notes = "유저의 예약한 전체 내역을 관리자 권한으로 조회")
	@GetMapping("/userId/{id}/type/{type}")
	public List<ReservationInfoListResponseDto> getReservationInfosByAdminRole(@PathVariable("id") String id
																		, @PathVariable("type") ReservationType reservationType
																		, @RequestHeader(name = "Authorization") String bearerToken) {
		return reservationGetService.getReservationInfosByUserId(id, reservationType, bearerToken);
	}

	@ApiOperation(value = "날짜별 예약 조회", notes = "날짜별 예약 조회, Admin 에서 사용")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "type", value = "예약타입", required = true),
			@ApiImplicitParam(name = "date", value = "2019-04-14 형태", required = true)
	})
	@PostMapping("/date")
	public List<ReservationInfoListResponseDto> getReservationInfosByDate(@RequestBody ReservationDateRequestDto request,
																			  @RequestHeader(name = "Authorization") String bearerToken) {

		return reservationGetService.getReservationInfosByDate(request, bearerToken);
	}


	@ApiOperation(value = "날짜구간별 예약 조회", notes = "날짜구간별 예약 조회, Admin 에서 사용")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "type", value = "예약타입", required = true),
			@ApiImplicitParam(name = "startDate", value = "시작날짜, 2019-04-14 형태", required = true),
			@ApiImplicitParam(name = "endDate", value = "끝날짜, 2019-04-14 형태", required = true)
	})
	@PostMapping("/date/range")
	public List<ReservationInfoListResponseDto> getReservationInfosByDateRange(@RequestBody ReservationDateRangeRequestDto request,
																				   @RequestHeader(name = "Authorization") String bearerToken) {

		return reservationGetService.getReservationInfosByDateRange(request, bearerToken);
	}


	@ApiOperation("예약 정보 저장")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "bringTime", value = "수령시간은 매일 아침 8시 ~ 저녁 8시 사이이며 예약시간(현재 시간)보다 빠를 수 없다", required = true),
			@ApiImplicitParam(name = "breadName", value = "예약 빵의 이름", required = true),
			@ApiImplicitParam(name = "breadCount", value = "예약 빵의 개수", required = true)
	})
	@PostMapping
	public void saveReservation(@RequestBody ReservationSaveRequestDto request,
								@RequestHeader(name = "Authorization") String bearerToken) {

		reservationSaveService.saveReservation(request, bearerToken);
	}


	@ApiOperation("예약정보 수정")
	@PutMapping
	public void updateReservation(@RequestBody ReservationUpdateRequestDto request,
								  @RequestHeader(name = "Authorization") String bearerToken) {

		reservationSaveService.updateReservation(request, bearerToken);
	}


	@ApiOperation(value = "예약 상태 수정", notes = "Admin에서 유저의 예약 상태를 변경")
	@PutMapping("/{num}")
	public void updateReservationType(@PathVariable("num") Long reservationId,
									  @RequestHeader(name = "Authorization") String bearerToken) {

		reservationUpdateService.updateReservationType(reservationId, bearerToken);
	}


	@ApiOperation("예약정보 삭제")
	@DeleteMapping("/{num}")
	public void deleteReservation(@PathVariable("num") Long reservationId,
								  @RequestHeader(name = "Authorization") String bearerToken) {

		reservationDeleteService.deleteReservation(reservationId, bearerToken);
	}


}
