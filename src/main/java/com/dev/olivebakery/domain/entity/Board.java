package com.dev.olivebakery.domain.entity;

import com.dev.olivebakery.domain.dtos.board.PostDetailsRequestDto;
import com.dev.olivebakery.domain.enums.BoardType;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "board_tbl")
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Board {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long boardId;

  @CreationTimestamp
  private LocalDateTime insertTime;

  @UpdateTimestamp
  private LocalDateTime updateTime;

  private String title;

  @Lob
  private String context;

  // 게시판인지 QnA인지
  @Enumerated(EnumType.STRING)
  private BoardType boardType;

  //공지사항일 경우 true
  private boolean isNotice = false;

  //비밀글일 경우 true
  private boolean isSecret = false;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "member")
  private Member member;

  @OneToMany(fetch = FetchType.EAGER, mappedBy = "board", cascade = {CascadeType.MERGE,
      CascadeType.REMOVE})
  private List<Comment> comments = new ArrayList<>();

  @Builder
  public Board(String title, String context, BoardType boardType, boolean isNotice,
      boolean isSecret, Member member) {
    this.title = title;
    this.context = context;
    this.boardType = boardType;
    this.isNotice = isNotice;
    this.isSecret = isSecret;
    this.member = member;
  }

  public void updateBoard(PostDetailsRequestDto updatePostDto) {
    this.context = updatePostDto.getContext();
    this.title = updatePostDto.getTitle();
    this.isNotice = updatePostDto.isNotice();
    this.isSecret = updatePostDto.isSecret();
  }
}
