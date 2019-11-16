package com.dev.olivebakery.domain.dtos.board;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CommentListResponseDto {

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
  private LocalDateTime insertTime;
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
  private LocalDateTime updateTime;
  private String userName;
  private String userId;
  private String content;

  @Builder
  public CommentListResponseDto(LocalDateTime insertTime, LocalDateTime updateTime, String userName,
      String userId, String content) {
    this.insertTime = insertTime;
    this.updateTime = updateTime;
    this.userName = userName;
    this.userId = userId;
    this.content = content;
  }
}
