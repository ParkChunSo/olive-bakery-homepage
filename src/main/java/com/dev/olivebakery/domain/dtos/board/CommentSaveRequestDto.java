package com.dev.olivebakery.domain.dtos.board;

import com.dev.olivebakery.domain.entity.Board;
import com.dev.olivebakery.domain.entity.Comment;
import com.dev.olivebakery.domain.entity.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter @NoArgsConstructor
public class CommentSaveRequestDto {
    private String boardId;
    private String content;

    public Comment toEntity(Member member,Board board){
        return Comment.builder()
                .userName(member.getName())
                .userId(member.getId())
                .content(this.content)
                .board(board)
                .build();
    }
}
