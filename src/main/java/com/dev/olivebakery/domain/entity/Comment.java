package com.dev.olivebakery.domain.entity;

import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

/**
 * Created by YoungMan on 2019-02-11.
 */

@Entity
@Table(name = "comment_tbl")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long commentId;

  @CreationTimestamp
  private LocalDateTime insertTime;

  @UpdateTimestamp
  private LocalDateTime updateTime;

  private String userId;

  private String userName;

  @Lob
  private String content;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "board_id")
  private Board board;

  @Builder
  public Comment(String userName, String userId, String content, Board board) {
    this.userName = userName;
    this.userId = userId;
    this.content = content;
    this.board = board;
  }

  public Comment update(String content) {
    this.content = content;
    return this;
  }
}
