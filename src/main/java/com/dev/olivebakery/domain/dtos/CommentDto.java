package com.dev.olivebakery.domain.dtos;

import com.dev.olivebakery.domain.entity.Board;
import com.dev.olivebakery.domain.entity.Comment;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


public class CommentDto {
    @Getter
    @NoArgsConstructor
    public static class GetComment {
        private LocalDateTime insertTime;
        private LocalDateTime updateTime;
        private String userName;
        private String userId;
        private String content;

        @Builder
        public GetComment(LocalDateTime insertTime, LocalDateTime updateTime, String userName, String userId, String content) {
            this.insertTime = insertTime;
            this.updateTime = updateTime;
            this.userName = userName;
            this.userId = userId;
            this.content = content;
        }
    }

    @Getter
    @NoArgsConstructor
    public static class SaveComment {
        private String boardId;
        private String userName;
        private String userId;
        private String content;

        public Comment toEntity(Board board){
            return Comment.builder()
                    .userName(userName)
                    .userId(userId)
                    .content(content)
                    .board(board)
                    .build();
        }
    }
    @Getter
    @NoArgsConstructor
    public static class UpdateComment {
        private String boardId;
        private String userName;
        private String userId;
        private String content;
        @ApiModelProperty(notes = "yyyy-mm-ddThh:mm:ss 형식으로 보내야함(중간에 T조심)")
        private LocalDateTime updateTime;
    }

    @Getter
    @NoArgsConstructor
    public static class DeleteComment {
        private String boardId;
        private String userId;
        @ApiModelProperty(notes = "yyyy-mm-ddThh:mm:ss 형식으로 보내야함(중간에 T조심)")
        private LocalDateTime updateTime;
    }
}
