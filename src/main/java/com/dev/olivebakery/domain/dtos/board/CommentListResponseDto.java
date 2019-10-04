package com.dev.olivebakery.domain.dtos.board;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class CommentListResponseDto {
    private LocalDateTime insertTime;
    private LocalDateTime updateTime;
    private String userName;
    private String userId;
    private String content;

    @Builder
    public CommentListResponseDto(LocalDateTime insertTime, LocalDateTime updateTime, String userName, String userId, String content) {
        this.insertTime = insertTime;
        this.updateTime = updateTime;
        this.userName = userName;
        this.userId = userId;
        this.content = content;
    }
}
