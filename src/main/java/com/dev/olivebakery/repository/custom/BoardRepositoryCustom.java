package com.dev.olivebakery.repository.custom;

import com.dev.olivebakery.domain.dtos.board.PostDetailsResponseDto;
import com.dev.olivebakery.domain.dtos.board.PostListResponseDto;
import com.dev.olivebakery.domain.enums.BoardType;
import org.springframework.data.domain.Page;

import java.util.List;

public interface BoardRepositoryCustom {
    Page<PostListResponseDto> getPosts(BoardType boardType, int pageNum);
    List<PostListResponseDto> getNoticePosts();
    PostDetailsResponseDto getPostDetails(Long boardId);
}
