package com.dev.olivebakery.domain.dtos.board;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 게시판 또는 QnA 리스트 Dto
 */

@Getter
@NoArgsConstructor
public class PostListResponseDto {

  private Long boardId;
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
  private LocalDateTime insertTime;
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
  private LocalDateTime updateTime;
  private String title;
  private String context;
  private boolean isNotice;
  private boolean isSecret;
  private String userId;

  @Builder
  public PostListResponseDto(Long boardId, LocalDateTime insertTime, LocalDateTime updateTime,
      String title, String context, boolean isNotice, boolean isSecret, String userId) {
    this.boardId = boardId;
    this.insertTime = insertTime;
    this.updateTime = updateTime;
    this.title = title;
    this.context = context;
    this.isNotice = isNotice;
    this.isSecret = isSecret;
    this.userId = userId;
  }
}
