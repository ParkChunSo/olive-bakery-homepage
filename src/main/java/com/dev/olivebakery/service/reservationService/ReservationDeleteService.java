package com.dev.olivebakery.service.reservationService;

import com.dev.olivebakery.exception.UserDefineException;
import com.dev.olivebakery.repository.ReservationRepository;
import com.dev.olivebakery.security.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Created by YoungMan on 2019-05-21.
 */

@Service
@RequiredArgsConstructor
public class ReservationDeleteService {

  private final ReservationRepository reservationRepository;
  private final JwtProvider jwtProvider;


  /**
   * 예약 삭제
   */
  public void deleteReservation(Long reservationId, String bearerToken) {
    String findEmail = reservationRepository.getMemberIdByReservationId(reservationId);
    String tokenEmail = jwtProvider.getUserEmailByToken(bearerToken);
    checkValidateEmail(findEmail, tokenEmail);

    reservationRepository.deleteById(reservationId);
  }

  private void checkValidateEmail(String findEmail, String tokenEmail) {
    if (!findEmail.equals(tokenEmail)) {
      throw new UserDefineException("예약자와 삭제자가 일치하지 않습니다.");
    }
  }
}
