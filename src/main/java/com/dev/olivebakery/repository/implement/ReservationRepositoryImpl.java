package com.dev.olivebakery.repository.implement;

import com.dev.olivebakery.domain.dtos.reservation.ReservationInfoListTmpDto;
import com.dev.olivebakery.domain.dtos.reservation.ReservationSaleTmpDto;
import com.dev.olivebakery.domain.entity.QBread;
import com.dev.olivebakery.domain.entity.QMember;
import com.dev.olivebakery.domain.entity.QReservation;
import com.dev.olivebakery.domain.entity.QReservationInfo;
import com.dev.olivebakery.domain.enums.ReservationType;
import com.dev.olivebakery.repository.custom.ReservationRepositoryCustom;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class ReservationRepositoryImpl implements ReservationRepositoryCustom {
    @PersistenceContext
    EntityManager entityManager;
    private QMember member = QMember.member;
    private QBread bread = QBread.bread;
    private QReservation reservation = QReservation.reservation;
    private QReservationInfo reservationInfo = QReservationInfo.reservationInfo;

    @Override
    public List<ReservationInfoListTmpDto> getReservationInfoList(String id, ReservationType type) {
        JPAQuery<ReservationInfoListTmpDto> query = newQueryInstance();
        query.where(member.id.eq(id)
                    .and(reservation.reservationType.eq(type)))
                .orderBy(reservation.reservationId.desc());
        return query.fetch();
    }

    @Override
    public List<ReservationInfoListTmpDto> getReservationInfoListByRecentlyDate(String id) {
        JPAQueryFactory tmpQuery = new JPAQueryFactory(entityManager);
        Long recentlyReservationId = tmpQuery.select(reservation.reservationId.max())
                                            .from(reservation)
                                            .join(reservation.member, member)
                                            .where(member.id.eq(id)).fetchOne();
        JPAQuery<ReservationInfoListTmpDto> query = newQueryInstance();
        query.where(reservation.reservationId.eq(recentlyReservationId))
                .orderBy(reservation.reservationId.desc());
        return query.fetch();
    }

    @Override
    public List<ReservationInfoListTmpDto> getReservationInfoListByDate(LocalDateTime startDate, LocalDateTime endDate, ReservationType reservationType) {
        JPAQuery<ReservationInfoListTmpDto> query = newQueryInstance();
        query.where(reservation.reservationTime.between(startDate, endDate)
                        .and(reservation.reservationType.eq(reservationType)))
                .orderBy(reservation.reservationId.desc());
        return query.fetch();
    }

    @Override
    public ReservationSaleTmpDto getReservationSaleByDate(LocalDateTime startDate, LocalDateTime endDate, ReservationType reservationType) {
        JPAQuery<ReservationSaleTmpDto> query = new JPAQuery<>(entityManager);
        query.select(Projections.constructor(ReservationSaleTmpDto.class, reservation.reservationId.count(), reservation.price.sum()))
                .from(reservation)
                .where(reservation.reservationTime.between(startDate, endDate)
                            .and(reservation.reservationType.eq(reservationType)));
        return query.fetchOne();
    }

    @Override
    public String getMemberIdByReservationId(Long reservationId) {
        JPAQueryFactory query = new JPAQueryFactory(entityManager);
        return query.select(member.id)
                    .from(reservation)
                    .join(reservation.member, member)
                    .where(reservation.reservationId.eq(reservationId)).fetchOne();
    }

    private JPAQuery<ReservationInfoListTmpDto> newQueryInstance(){
        JPAQuery<ReservationInfoListTmpDto> query = new JPAQuery<>(entityManager);
        query.select(Projections.constructor(ReservationInfoListTmpDto.class, reservation.reservationId, reservation.reservationTime, reservation.bringTime, reservation.price, member.name, bread.name, reservationInfo.breadCount))
                .from(reservation)
                .join(reservation.member, member)
                .join(reservation.reservationInfos, reservationInfo)
                .join(reservationInfo.bread, bread);
        return query;
    }
}
