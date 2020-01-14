package com.dev.olivebakery.domain.entity;

import java.time.LocalDate;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.Getter;
import org.hibernate.annotations.CreationTimestamp;

@Entity
@Getter
@Table(name = "sold_out_tbl")
public class SoldOut {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @CreationTimestamp
  private LocalDate date;

  @OneToOne
  @JoinColumn(name = "bread_id")
  private Bread bread;
}
