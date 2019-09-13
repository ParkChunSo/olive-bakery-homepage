package com.dev.olivebakery.service;

import com.dev.olivebakery.exception.UserDefineException;
import com.dev.olivebakery.service.reservationService.ReservationSaveService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.time.LocalDateTime;


@RunWith(SpringRunner.class)
@SpringBootTest
public class ReservationSaveServiceTest {
//
//	@Autowired
//	ReservationSaveService reservationSaveService;
//
//
//	@Test
//	public void timeValidationCheck_올바른_예약시간() throws Exception {
//
//		//given
//		LocalDateTime bringTime = LocalDate.now()
//				.plusDays(1)
//				.atTime(12, 12, 12)
//		;
//
//		//then
//		reservationSaveService.timeValidationCheck(bringTime);
//	}
//
//
//	@Test(expected = UserDefineException.class)
//	public void timeValidationCheck_잘못된_예약시간_20시이후() throws Exception {
//
//		//given
//		LocalDateTime bringTime = LocalDate.now()
//				.plusDays(1)
//				.atTime(20, 12, 12)
//		;
//
//		//then
//		reservationSaveService.timeValidationCheck(bringTime);
//	}

}
