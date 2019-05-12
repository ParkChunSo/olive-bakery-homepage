package com.dev.olivebakery.domain.entity;

import com.dev.olivebakery.domain.enums.ReservationType;
import com.dev.olivebakery.utill.Explain;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.sql.Timestamp;
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
    @JoinColumn(name = "email")
    private Member member;

    @OneToMany(mappedBy = "reservation", fetch = FetchType.EAGER)
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

    @Explain("요청 -> 수락 -> 완료")
    public void updateReservationType() {
        reservationType = reservationType.equals(ReservationType.REQUEST) ? ReservationType.ACCEPT : ReservationType.COMPLETE;
    }
}
