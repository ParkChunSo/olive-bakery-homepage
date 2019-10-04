package com.dev.olivebakery.domain.dtos.board;

import com.dev.olivebakery.domain.entity.Board;
import com.dev.olivebakery.domain.entity.Comment;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class CommentRequestDto {
    private String boardId;
    private String userName;
    private String userId;
    private String content;
    private LocalDateTime updateTime;

    public Comment toEntity(Board board){
        return Comment.builder()
                .userName(this.userName)
                .userId(this.userId)
                .content(this.content)
                .board(board)
                .build();
    }

    public CommentRequestDto setUserId(String userId) {
        this.userId = userId;
        return this;
    }
}
