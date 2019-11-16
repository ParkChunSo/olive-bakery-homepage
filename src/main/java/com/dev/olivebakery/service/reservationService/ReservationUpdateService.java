package com.dev.olivebakery.service.reservationService;

import com.dev.olivebakery.domain.entity.Reservation;
import com.dev.olivebakery.domain.enums.MemberRole;
import com.dev.olivebakery.exception.UserDefineException;
import com.dev.olivebakery.repository.ReservationRepository;
import com.dev.olivebakery.security.JwtProvider;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Created by YoungMan on 2019-05-21.
 */

@Service
@RequiredArgsConstructor
public class ReservationUpdateService {

  private final ReservationRepository reservationRepository;
  private final JwtProvider jwtProvider;


  /**
   * 예약 상태 수정 요청 -> 수락 -> 완료
   */
  public void updateReservationType(Long reservationId, String bearerToken) {
    checkValidateRole(bearerToken);
    Reservation reservation = findById(reservationId);
    reservation.updateReservationType();
    reservationRepository.save(reservation);
  }

  private Reservation findById(Long reservationId) {
    return reservationRepository.findById(reservationId)
        .orElseThrow((() -> new UserDefineException("해당 예약이 존재하지 않습니다.")));
  }

  private void checkValidateRole(String bearerToken) {
    List<MemberRole> roles = jwtProvider.getUserRolesByToken(bearerToken);
    if (!roles.contains(MemberRole.ADMIN)) {
      throw new UserDefineException("관리자 권한이 아닙니다");
    }
  }
}
