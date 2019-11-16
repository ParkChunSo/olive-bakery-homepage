package com.dev.olivebakery.domain.entity;

import com.dev.olivebakery.domain.enums.DayType;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "bread_day_tbl")
@Builder
public class Days {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "days_id")
  private Long id;

  @Enumerated(value = EnumType.STRING)
  private DayType dayType;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "bread_id")
  private Bread bread;

  public static List<Days> newListInstance(Bread bread, List<DayType> dayTypeList) {
    List<Days> days = new ArrayList<>();
    for (DayType type : dayTypeList) {
      days.add(
          Days.builder().bread(bread).dayType(type).build()
      );
    }
    return days;
  }
}
