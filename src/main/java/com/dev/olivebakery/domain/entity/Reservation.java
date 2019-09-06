package com.dev.olivebakery.domain.entity;

import com.dev.olivebakery.domain.enums.ReservationType;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by YoungMan on 2019-02-08.
 */

@Entity
@Table(name = "reservation_tbl")
@EntityListeners(AuditingEntityListener.class)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reservationId;

    @CreationTimestamp
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime reservationTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime bringTime;

    private Integer price;

    @Enumerated(EnumType.STRING)
    private ReservationType reservationType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "uuid")
    private Member member;

    @OneToMany(mappedBy = "reservation", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<ReservationInfo> reservationInfos = new ArrayList<>();


    @Builder
    public Reservation(LocalDateTime reservationTime, LocalDateTime bringTime, Integer price, Member member, List<ReservationInfo> reservationInfos) {
        this.reservationTime = reservationTime;
        this.bringTime = bringTime;
        this.price = price;
        this.reservationType = ReservationType.REQUEST;
        this.member = member;
        this.reservationInfos = reservationInfos;
    }

    public static Reservation of(LocalDateTime bringTime, Member member, int totalPrice) {
        return Reservation.builder()
                .bringTime(bringTime)
                .member(member)
                .price(totalPrice)
                .build()
        ;
    }

    public void updateReservationInfos(List<ReservationInfo> reservationInfos) {
        this.reservationInfos = reservationInfos;
    }

    public void updateBringTime(LocalDateTime bringTime) {
        this.bringTime = bringTime;
    }

    public void updateTotalPrice(int totalPrice) {
        this.price = totalPrice;
    }

    public void updateReservationType() {
        reservationType = reservationType.equals(ReservationType.REQUEST) ? ReservationType.ACCEPT : ReservationType.COMPLETE;
    }
}
