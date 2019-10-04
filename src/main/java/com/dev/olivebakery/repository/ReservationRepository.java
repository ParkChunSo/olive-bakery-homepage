package com.dev.olivebakery.repository;

import com.dev.olivebakery.domain.entity.Reservation;
import com.dev.olivebakery.repository.custom.ReservationRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by YoungMan on 2019-02-09.
 */

public interface ReservationRepository extends JpaRepository<Reservation, Long>, ReservationRepositoryCustom {

//	@Query(value = "select new com.dev.olivebakery.domain.dtos.ReservationDto$ReservationResponseTemp(reservation.reservationId, reservation.reservationTime, reservation.bringTime, reservation.price, memeber.name, " +
//			"bread.name, reservationinfos.breadCount) " +
//			"from Reservation reservation " +
//			"join reservation.member memeber " +
//			"join reservation.reservationInfos reservationinfos " +
//			"join reservationinfos.bread bread " +
//			"where memeber.id = :id and reservation.reservationType = :reservationType " +
//			"order by reservation.reservationId desc")
//	List<ReservationDto.ReservationResponseTemp> getReservationInfos(@Param("id") String id, @Param("reservationType") ReservationType reservationType);
//
//
//	@Query(value = "select new com.dev.olivebakery.domain.dtos.ReservationDto$ReservationResponseTemp(reservation.reservationId, reservation.reservationTime, reservation.bringTime, reservation.price, memeber.name, " +
//			"bread.name, reservationinfos.breadCount) " +
//			"from Reservation reservation " +
//			"join reservation.member memeber " +
//			"join reservation.reservationInfos reservationinfos " +
//			"join reservationinfos.bread bread " +
//			"where reservation.reservationId = (select max(reservation.reservationId) from Reservation reservation join reservation.member mem where mem.id = :id) " +
//			"order by reservation.reservationId desc")
//	List<ReservationDto.ReservationResponseTemp> getReservationInfoByRecently(@Param("id") String id);
//
//
//	@Query(value = "select new com.dev.olivebakery.domain.dtos.ReservationDto$ReservationResponseTemp(reservation.reservationId, reservation.reservationTime, reservation.bringTime, reservation.price, memeber.name, " +
//			"bread.name, reservationinfos.breadCount) " +
//			"from Reservation reservation " +
//			"join reservation.member memeber " +
//			"join reservation.reservationInfos reservationinfos " +
//			"join reservationinfos.bread bread " +
//			"where reservation.reservationType = :reservationType and reservation.reservationTime > :startDate and reservation.reservationTime < :endDate " +
//			"order by reservation.reservationId desc")
//	List<ReservationDto.ReservationResponseTemp> getReservationInfosByDate(@Param("reservationType") ReservationType reservationType, @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
//
//
//	@Query(value = "select new com.dev.olivebakery.domain.dtos.ReservationDto$ReservationSale(count(reservation.reservationId), sum(reservation.price)) " +
//			"from Reservation reservation " +
//			"where reservation.reservationType = :reservationType and reservation.reservationTime > :startDate and reservation.reservationTime < :endDate")
//	ReservationDto.ReservationSale getReservationSaleByDate(@Param("reservationType") ReservationType reservationType, @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
//
//
//	@Query(value = "select memeber.id " +
//			"from Reservation reservation " +
//			"join reservation.member memeber " +
//			"where reservation.reservationId = :reservationId")
//	String getMemberEmailByReservationId(@Param("reservationId") Long reservationId);
}
