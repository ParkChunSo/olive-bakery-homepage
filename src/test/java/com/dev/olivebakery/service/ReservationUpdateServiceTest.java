//package com.dev.olivebakery.service;
//
//import com.dev.olivebakery.domain.dtos.ReservationDto;
//import com.dev.olivebakery.service.reservationService.ReservationDeleteService;
//import com.dev.olivebakery.service.reservationService.ReservationSaveService;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit4.SpringRunner;
//
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//import java.util.Arrays;
//
///**
// * Created by YoungMan on 2019-06-01.
// */
//
//@RunWith(SpringRunner.class)
//@SpringBootTest
////@Transactional
//public class ReservationUpdateServiceTest {
//
//	@Autowired
//	ReservationDeleteService reservationDeleteService;
//
//	@Autowired
//	ReservationSaveService reservationSaveService;
//
//	@Test
//	public void updateReservation() throws Exception {
//
//		//given
//		Long reservationId = 32L;
//		LocalDateTime bringTime = LocalDate.now()
//				.plusDays(1)
//				.atTime(15, 15, 15)
//				;
//
//		ReservationDto.ReservationSaveRequest reservationSaveRequest = ReservationDto.ReservationSaveRequest.builder()
//				.bringTime(bringTime)
//				.userEmail("test@test.com")
//				.breadInfo(Arrays.asList(
//						ReservationDto.ReservationBread.builder()
//								.breadName("test1")//3
//								.breadCount(2)
//								.build(),
//						ReservationDto.ReservationBread.builder()
//								.breadName("식빵2")//1
//								.breadCount(2)
//								.build(),
//						ReservationDto.ReservationBread.builder()
//								.breadName("소보로빵")//2
//								.breadCount(1)
//								.build()
//						)
//				)
//				.build();
//
//		//when
//		reservationSaveService.updateReservation(
//				ReservationDto.ReservationUpdateRequest.builder()
//				.reservationId(reservationId)
//				.reservationSaveRequest(reservationSaveRequest)
//				.build()
//		);
//
//	}
//}
