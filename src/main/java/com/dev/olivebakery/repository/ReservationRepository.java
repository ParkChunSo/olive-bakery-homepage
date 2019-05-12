package com.dev.olivebakery.repository;

import com.dev.olivebakery.domain.dto.ReservationDto;
import com.dev.olivebakery.domain.entity.Member;
import com.dev.olivebakery.domain.entity.Reservation;
import com.dev.olivebakery.domain.enums.ReservationType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Created by YoungMan on 2019-02-09.
 */

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
	Optional<List<Reservation>> findByMember(Member member);

	@Query(value = "select new com.dev.olivebakery.domain.dto.ReservationDto$ReservationResponseTemp(reservation.reservationId, reservation.reservationTime, reservation.bringTime, reservation.price, memeber.name, " +
			"bread.name, reservationinfos.breadCount) " +
			"from Reservation reservation " +
			"join reservation.member memeber " +
			"join reservation.reservationInfos reservationinfos " +
			"join reservationinfos.bread bread " +
			"where memeber.email = :email and reservation.reservationType = :reservationType")
	List<ReservationDto.ReservationResponseTemp> getReservationInfos(@Param("email") String email, @Param("reservationType") ReservationType reservationType);


	@Query(value = "select new com.dev.olivebakery.domain.dto.ReservationDto$ReservationResponseTemp(reservation.reservationId, reservation.reservationTime, reservation.bringTime, reservation.price, memeber.name, " +
			"bread.name, reservationinfos.breadCount) " +
			"from Reservation reservation " +
			"join reservation.member memeber " +
			"join reservation.reservationInfos reservationinfos " +
			"join reservationinfos.bread bread " +
			"where reservation.reservationId = (select max(reservation.reservationId) from reservation where memeber.email = :email)")
	List<ReservationDto.ReservationResponseTemp> getReservationInfoByRecently(@Param("email") String email);


	@Query(value = "select new com.dev.olivebakery.domain.dto.ReservationDto$ReservationResponseTemp(reservation.reservationId, reservation.reservationTime, reservation.bringTime, reservation.price, memeber.name, " +
			"bread.name, reservationinfos.breadCount) " +
			"from Reservation reservation " +
			"join reservation.member memeber " +
			"join reservation.reservationInfos reservationinfos " +
			"join reservationinfos.bread bread " +
			"where reservation.reservationType = :reservationType and reservation.reservationTime > :startDate and reservation.reservationTime < :endDate")
	List<ReservationDto.ReservationResponseTemp> getReservationInfosByDate(@Param("reservationType") ReservationType reservationType, @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);


	@Query(value = "select new com.dev.olivebakery.domain.dto.ReservationDto$ReservationSale(count(reservation.reservationId), sum(reservation.price)) " +
			"from Reservation reservation " +
			"where reservation.reservationType = :reservationType and reservation.reservationTime > :startDate and reservation.reservationTime < :endDate")
	ReservationDto.ReservationSale getReservationSaleByDate(@Param("reservationType") ReservationType reservationType, @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
}
