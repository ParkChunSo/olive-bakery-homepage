package com.dev.olivebakery.domain.dtos.board;

import com.dev.olivebakery.domain.entity.Board;
import com.dev.olivebakery.domain.entity.Member;
import com.dev.olivebakery.domain.enums.BoardType;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostDetailsRequestDto {
    @ApiModelProperty(notes = "게시물 업데이트할 때만 필요")
    private Long boardId;
    private String context;
    private String title;
    private BoardType boardType;
    @ApiModelProperty(notes = "true 또는 false로 보내야함")
    @JsonProperty
    private boolean isNotice;
    @ApiModelProperty(notes = "true 또는 false로 보내야함")
    @JsonProperty
    private boolean isSecret;

    @Builder
    public PostDetailsRequestDto(Long boardId, String context, String title, BoardType boardType, boolean isSecret, boolean isNotice) {
        this.boardId = boardId;
        this.context = context;
        this.title = title;
        this.boardType = boardType;
        this.isNotice = isNotice;
        this.isSecret = isSecret;
    }

    public Board toEntity(Member member) {
        return Board.builder()
                .context(context)
                .title(title)
                .member(member)
                .boardType(boardType)
                .isNotice(isNotice)
                .isSecret(isSecret)
                .build();
    }
}
