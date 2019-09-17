package com.dev.olivebakery.domain.dtos.board;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class PostDetailsResponseDto {
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
    private List<CommentListResponseDto> commentList = new ArrayList<>();

    @Builder
    public PostDetailsResponseDto(Long boardId, LocalDateTime insertTime, LocalDateTime updateTime, String title, String context, boolean isNotice, boolean isSecret, String userId, List<CommentListResponseDto> commentList) {
        this.boardId = boardId;
        this.insertTime = insertTime;
        this.updateTime = updateTime;
        this.title = title;
        this.context = context;
        this.isNotice = isNotice;
        this.isSecret = isSecret;
        this.userId = userId;
        this.commentList = commentList;
    }

    public PostDetailsResponseDto convertListToDetail(PostListResponseDto postListResponseDto){
        this.boardId = postListResponseDto.getBoardId();
        this.insertTime = postListResponseDto.getInsertTime();
        this.updateTime = postListResponseDto.getUpdateTime();
        this.title = postListResponseDto.getTitle();
        this.context = postListResponseDto.getContext();
        this.isNotice = postListResponseDto.isNotice();
        this.isSecret = postListResponseDto.isSecret();
        this.userId = postListResponseDto.getUserId();

        return this;
    }

    public PostDetailsResponseDto setCommentList(List<CommentListResponseDto> commentList){
        this.commentList = commentList;
        return this;
    }
}
