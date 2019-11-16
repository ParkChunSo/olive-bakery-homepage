package com.dev.olivebakery.service.reservationService;

import com.dev.olivebakery.domain.dtos.reservation.ReservationSaveRequestDto;
import com.dev.olivebakery.domain.dtos.reservation.ReservationUpdateRequestDto;
import com.dev.olivebakery.domain.entity.Bread;
import com.dev.olivebakery.domain.entity.Reservation;
import com.dev.olivebakery.domain.entity.ReservationInfo;
import com.dev.olivebakery.exception.UserDefineException;
import com.dev.olivebakery.repository.BreadRepository;
import com.dev.olivebakery.repository.MemberRepository;
import com.dev.olivebakery.repository.ReservationInfoRepository;
import com.dev.olivebakery.repository.ReservationRepository;
import com.dev.olivebakery.security.JwtProvider;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Created by YoungMan on 2019-05-20.
 */

@Service
@RequiredArgsConstructor
public class ReservationSaveService {

  private final ReservationRepository reservationRepository;
  private final ReservationInfoRepository reservationInfoRepository;
  private final BreadRepository breadRepository;
  private final MemberRepository memberRepository;
  private final JwtProvider jwtProvider;


  /**
   * 시간 체크 후 예약 정보 저장
   */
  public void saveReservation(ReservationSaveRequestDto saveDto, String bearerToken) {
    timeValidationCheck(saveDto.getBringTime());
    Reservation reservation = getReservationBySaveDto(saveDto,
        jwtProvider.getUserEmailByToken(bearerToken));
    List<ReservationInfo> reservationInfos = getReservationInfos(saveDto, reservation);
    reservationInfoRepository.saveAll(reservationInfos);
  }

  /**
   * 수령시간은 AM 10시 ~ PM 8시 수령시간은 예약시간보다 빠를 수 없다
   */
  public void timeValidationCheck(LocalDateTime bringTime) {
    Predicate<LocalDateTime> predicate = b -> b.isAfter(LocalDateTime.now()) && b.getHour() >= 10
        && b.getHour() <= 19;
    if (!predicate.test(bringTime)) {
      throw new UserDefineException(bringTime.toString() + "  수령시간이 올바르지 않습니다.");
    }
  }

  /**
   * ReservationSaveRequestDto -> Reservation 으로 변환
   */
  private Reservation getReservationBySaveDto(ReservationSaveRequestDto saveDto, String email) {
    return Reservation.of(saveDto.getBringTime(),
        memberRepository.findById(email)
            .orElseThrow(() -> new UserDefineException("해당 유저가 존재하지 않습니다.")),
        getTotalPrice(saveDto.getBreadNames(), saveDto.getBreadCounts())
    );
  }

  /**
   * 빵이름과 개수를 바탕으로 총 예약 금액 반환
   */
  private int getTotalPrice(List<String> breadNames, List<Integer> counts) {
    List<Bread> breads = findsByNames(breadNames);

    return IntStream.range(0, breads.size())
        .map(index -> breads.get(index).getPrice() * counts.get(index))
        .sum();
  }

  /**
   * String 빵 이름을 바탕으로 Bread 를 찾는다
   */
  private List<Bread> findsByNames(List<String> breadNames) {
    List<Bread> findBreads = breadRepository.findAllByByNameInQuery(breadNames);
    return sortFindBreads(findBreads, breadNames);
  }

  /**
   * 빵 리스트 정렬
   */
  private List<Bread> sortFindBreads(List<Bread> findBreads, List<String> breadNames) {
    List<Bread> sortFindBreads = new ArrayList<>();

    for (String breadName : breadNames) {
      for (Bread bread : findBreads) {
        if (isEqualBreadName(breadName, bread.getName())) {
          sortFindBreads.add(bread);
          break;
        }
      }
    }
    return sortFindBreads;
  }

  private boolean isEqualBreadName(String strBreadName, String objectBreadName) {
    return strBreadName.equals(objectBreadName);
  }

  /**
   * ReservationSaveRequest 와 Reservation -> List<ReservationInfo>
   */
  private List<ReservationInfo> getReservationInfos(ReservationSaveRequestDto saveDto,
      Reservation reservation) {
    List<ReservationInfo> reservationInfos = new ArrayList<>();
    List<Bread> breads = findsByNames(saveDto.getBreadNames());

    for (int i = 0; i < breads.size(); i++) {
      reservationInfos.add(ReservationInfo.of(saveDto.getBreadCounts().get(i),
          reservation, breads.get(i))
      );
    }
    return reservationInfos;
  }

  /**
   * 예약 수정
   */
  @Transactional
  public void updateReservation(ReservationUpdateRequestDto reservationUpdateRequest,
      String bearerToken) {
    String findEmail = reservationRepository
        .getMemberIdByReservationId(reservationUpdateRequest.getReservationId());
    checkValidateEmail(findEmail, bearerToken);

    Reservation reservation = findById(reservationUpdateRequest.getReservationId());
    ReservationSaveRequestDto saveRequest = reservationUpdateRequest.getReservationSaveRequest();

    timeValidationCheck(saveRequest.getBringTime());
    reservation.updateBringTime(saveRequest.getBringTime());
    reservation.updateTotalPrice(getTotalPrice(saveRequest.getBreadNames(),
        saveRequest.getBreadCounts())
    );

    List<Long> ids = getReservationInfoIds(reservation.getReservationInfos());
    reservationInfoRepository.deleteAllByIdInQuery(ids);

    reservation.updateReservationInfos(getReservationInfos(saveRequest,
        reservation)
    );

    reservationRepository.save(reservation);
  }

  private void checkValidateEmail(String findEmail, String bearerToken) {
    if (!findEmail.equals(bearerToken)) {
      throw new UserDefineException("예약자와 수정자가 일치하지 않습니다.");
    }
  }

  private Reservation findById(Long reservationId) {
    return reservationRepository.findById(reservationId)
        .orElseThrow((() -> new UserDefineException("해당 예약이 존재하지 않습니다.")));
  }

  /**
   * 예약 정보의 uuid 값 반환
   */
  private List<Long> getReservationInfoIds(List<ReservationInfo> reservationInfos) {
    return reservationInfos.stream()
        .map(i -> i.getReservationInfoId())
        .collect(Collectors.toList());
  }
}
