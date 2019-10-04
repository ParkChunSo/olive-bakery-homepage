package com.dev.olivebakery.repository.implement;

import com.dev.olivebakery.domain.dtos.board.CommentListResponseDto;
import com.dev.olivebakery.domain.dtos.board.PostDetailsResponseDto;
import com.dev.olivebakery.domain.dtos.board.PostListResponseDto;
import com.dev.olivebakery.domain.entity.*;
import com.dev.olivebakery.domain.enums.BoardType;
import com.dev.olivebakery.repository.custom.BoardRepositoryCustom;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;

import static com.querydsl.core.group.GroupBy.groupBy;
import static com.querydsl.core.group.GroupBy.list;

@Component
public class BoardRepositoryImpl extends QuerydslRepositorySupport implements BoardRepositoryCustom {
    @PersistenceContext
    EntityManager entityManager;
    private QBoard board = QBoard.board;
    private QMember member = QMember.member;
    private QComment comment = QComment.comment;

    private final int DEFAULT_LIMIT_SIZE = 10;

    public BoardRepositoryImpl() {
        super(Board.class);
    }

    @Override
    public Page<PostListResponseDto> getPosts(BoardType boardType, int pageNum) {
        JPAQuery<PostListResponseDto> jpaQuery = new JPAQuery<>(entityManager);
        jpaQuery = setQuery(jpaQuery);
        jpaQuery.where(board.boardType.eq(boardType))
                .where(board.isNotice.eq(false))
                .orderBy(board.boardId.desc())
                .offset(--pageNum * DEFAULT_LIMIT_SIZE)
                .limit(DEFAULT_LIMIT_SIZE)
        ;
        List<PostListResponseDto> boards = jpaQuery.fetch();
        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);
        long totalCount = queryFactory.select(board.boardId.count()).from(board).where(board.boardType.eq(boardType)).fetchCount();
        return new PageImpl<>(boards, PageRequest.of(pageNum, DEFAULT_LIMIT_SIZE, new Sort(Sort.Direction.DESC, "boardId")), totalCount);
    }

    @Override
    public List<PostListResponseDto> getNoticePosts() {
        JPAQuery<PostListResponseDto> jpaQuery = new JPAQuery<>(entityManager);
        jpaQuery = setQuery(jpaQuery);
        jpaQuery.where(board.isNotice.eq(true));
        return jpaQuery.fetch();
    }

    @Override
    public PostDetailsResponseDto getPostDetails(Long boardId) {
        JPAQuery<PostListResponseDto> jpaQuery = new JPAQuery<>(entityManager);
        jpaQuery = setQuery(jpaQuery);
        PostListResponseDto post = jpaQuery.where(board.boardId.eq(boardId)).fetchOne();

        List<Comment> comments = jpaQuery.join(board.comments, comment)
                                        .transform(groupBy(board.boardId).as(list(comment))).get(boardId);

        List<CommentListResponseDto> commentDtoList = new ArrayList<>();
        if(comments != null) {
            comments.forEach(comment -> commentDtoList.add(
                                            CommentListResponseDto.builder()
                                                    .insertTime(comment.getInsertTime())
                                                    .updateTime(comment.getUpdateTime())
                                                    .userName(comment.getUserName())
                                                    .content(comment.getContent())
                                                    .build()
                    )
            );
        }

        return new PostDetailsResponseDto().convertListToDetail(post).setCommentList(commentDtoList);
    }

    private JPAQuery<PostListResponseDto> setQuery(JPAQuery<PostListResponseDto> query){
        return query.select(Projections.constructor(PostListResponseDto.class, board.boardId, board.insertTime, board.updateTime, board.title, board.context, board.isNotice, board.isSecret, member.id))
                    .from(board)
                    .join(board.member, member);
    }
}
