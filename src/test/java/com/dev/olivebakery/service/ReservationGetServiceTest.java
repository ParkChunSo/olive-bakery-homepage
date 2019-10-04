//package com.dev.olivebakery.service;
//
//import com.dev.olivebakery.domain.dtos.ReservationDto;
//import com.dev.olivebakery.domain.enums.ReservationType;
//import com.dev.olivebakery.repository.BreadRepository;
//import com.dev.olivebakery.repository.MemberRepository;
//import com.dev.olivebakery.service.reservationService.ReservationGetService;
//import com.dev.olivebakery.service.reservationService.ReservationSaveService;
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit4.SpringRunner;
//
//import javax.transaction.Transactional;
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//import java.util.Arrays;
//import java.util.List;
//
//import static org.hamcrest.core.Is.is;
//import static org.junit.Assert.assertThat;
//
//
//@RunWith(SpringRunner.class)
//@SpringBootTest
//@Transactional
//public class ReservationGetServiceTest {
//
//	@Autowired
//	ReservationSaveService reservationSaveService;
//
//	@Autowired
//	ReservationGetService reservationGetService;
//
//	@Autowired
//	MemberRepository memberRepository;
//
//	@Autowired
//	BreadRepository breadRepository;
//
//
//	@Before
//	public void setUp() throws Exception {
//
//		LocalDateTime bringTime = LocalDate.now()
//				.plusDays(1)
//				.atTime(10, 10, 10)
//		;
//
//		ReservationDto.ReservationSaveRequest reservationSaveRequest = ReservationDto.ReservationSaveRequest.builder()
//				.bringTime(bringTime)
//				.userEmail("test@test.com")
//				.breadInfo(Arrays.asList(
//						ReservationDto.ReservationBread.builder()
//								.breadName("소보로빵")
//								.breadCount(3)
//								.build(),
//						ReservationDto.ReservationBread.builder()
//								.breadName("test1")
//								.breadCount(1)
//								.build(),
//						ReservationDto.ReservationBread.builder()
//								.breadName("식빵2")
//								.breadCount(1)
//								.build()
//						)
//
//				)
//				.build()
//		;
//
//		reservationSaveService.saveReservation(reservationSaveRequest);
//	}
//
//
//	@Test
//	public void getReservationInfos_예약목록_조회() throws Exception {
//
//		//given
//		final String id = "test@test.com";
//		final ReservationType reservationType = ReservationType.REQUEST;
//		List<ReservationDto.ReservationBread> expectedBreads = Arrays.asList(
//				new ReservationDto.ReservationBread("소보로빵", 3),
//				new ReservationDto.ReservationBread("test1", 1),
//				new ReservationDto.ReservationBread("식빵2", 1)
//		);
//
//		//when
//		List<ReservationDto.ReservationResponse> reservationResponses = reservationGetService.getReservationInfos(id, reservationType);
//
//		//then
//		assertThat(reservationResponses.get(0).getReservationBreads().size(), is(3));
////		assertThat(reservationResponses.get(0).getPrice(), is(50000));
////		assertThat(reservationResponses.get(0).getReservationBreads(), is(expectedBreads));
//	}
//
//
//	@Test
//	public void getReservationInfoRecently_최신예약_한건_조회() throws Exception {
//
//		//given
//		final String id = "signTest@gmail.com";
//		List<ReservationDto.ReservationBread> expectedBreads = Arrays.asList(
//				new ReservationDto.ReservationBread("소보로빵", 1),
//				new ReservationDto.ReservationBread("죽빵", 2)
//		);
//
//		//when
//		ReservationDto.ReservationResponse reservationResponse = reservationGetService.getReservationInfoByRecently(id);
//
//		//then
//		assertThat(reservationResponse.getReservationBreads().size(), is(2));
//		assertThat(reservationResponse.getPrice(), is(50000));
//		assertThat(reservationResponse.getReservationBreads(), is(expectedBreads));
//	}
//
//
//	@Test
//	public void getReservationInfosByDate_선택날짜_예약목록_조회_오늘() throws Exception {
//
//		//given
//		ReservationDto.ReservationDateRequest reservationDateRequestDto = ReservationDto.ReservationDateRequest.builder()
//				.reservationType(ReservationType.REQUEST)
//				.selectDate(LocalDate.now())
//				.build()
//		;
//
//		List<ReservationDto.ReservationBread> expectedBreads = Arrays.asList(
//				new ReservationDto.ReservationBread("소보로빵", 1),
//				new ReservationDto.ReservationBread("죽빵", 2)
//		);
//
//		//when
//		List<ReservationDto.ReservationResponse> reservationResponses = reservationGetService.getReservationInfosByDate(reservationDateRequestDto);
//
//		//then
//		assertThat(reservationResponses.get(0).getReservationBreads().size(), is(2));
//		assertThat(reservationResponses.get(0).getPrice(), is(50000));
//		assertThat(reservationResponses.get(0).getReservationBreads(), is(expectedBreads));
//	}
//
//
//	@Test
//	public void getReservationInfosByDateRange_기간날짜_예약목록_조회_오늘에서_내일() throws Exception {
//
//		//given
//		ReservationDto.ReservationDateRangeRequest reservationDateRangeRequest = ReservationDto.ReservationDateRangeRequest.builder()
//				.reservationType(ReservationType.REQUEST)
//				.startDate(LocalDate.now())
//				.endDate(LocalDate.now().plusDays(1))
//				.build()
//		;
//
//		List<ReservationDto.ReservationBread> expectedBreads = Arrays.asList(
//				new ReservationDto.ReservationBread("소보로빵", 1),
//				new ReservationDto.ReservationBread("죽빵", 2)
//		);
//
//		//when
//		List<ReservationDto.ReservationResponse> reservationResponses = reservationGetService.getReservationInfosByDateRange(reservationDateRangeRequest);
//
//		//then
//		assertThat(reservationResponses.get(0).getReservationBreads().size(), is(2));
//		assertThat(reservationResponses.get(0).getPrice(), is(50000));
//		assertThat(reservationResponses.get(0).getReservationBreads(), is(expectedBreads));
//	}
//
//
//}
