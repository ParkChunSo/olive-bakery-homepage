package com.dev.olivebakery.domain.entity;

import com.dev.olivebakery.domain.enums.SaleType;
import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 스케줄러로 매일 04시 데이터 저장.. 예약 테이블의 데이터(온라인)은 스케줄러로 자동 저장되며 오프라인 금액은 관리자가 직접입력.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "sales_tbl")
public class Sales {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  // 날짜
  private LocalDate date;

  // 매출금액
  private int sales;

  // 예약 횟수
  @Column(nullable = true)
  private int reservationCnt;

  // 온라인인지 오프라인인지
  @Enumerated(value = EnumType.STRING)
  private SaleType saleType;

  @Builder
  public Sales(LocalDate date, int sales, int reservationCnt, SaleType saleType) {
    this.date = date;
    this.sales = sales;
    this.reservationCnt = reservationCnt;
    this.saleType = saleType;
  }
}
