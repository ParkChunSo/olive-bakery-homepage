package com.dev.olivebakery.service;

import com.dev.olivebakery.domain.dtos.board.*;
import com.dev.olivebakery.domain.entity.Board;
import com.dev.olivebakery.domain.entity.Comment;
import com.dev.olivebakery.domain.entity.Member;
import com.dev.olivebakery.domain.enums.BoardType;
import com.dev.olivebakery.domain.enums.MemberRole;
import com.dev.olivebakery.exception.UserDefineException;
import com.dev.olivebakery.repository.BoardRepository;
import com.dev.olivebakery.repository.CommentRepository;
import com.dev.olivebakery.security.JwtProvider;
import com.dev.olivebakery.service.signService.SignService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BoardService {

    private final BoardRepository boardRepository;
    private final CommentRepository commentRepository;
    private final SignService signService;
    private final JwtProvider jwtProvider;

    public BoardService(BoardRepository boardRepository, CommentRepository commentRepository, SignService signService, JwtProvider jwtProvider) {
        this.boardRepository = boardRepository;
        this.commentRepository = commentRepository;
        this.signService = signService;
        this.jwtProvider = jwtProvider;
    }

    public Board findBoardById(Long boardId) {
        return boardRepository.findById(boardId)
                .orElseThrow(() -> new UserDefineException("해당 번호의 게시글은 존재하지 않습니다."));
    }

    /**
     *
     * @param boardType: 게시판 or QnA
     * @param pageNum: 페이지 번호
     *
     */
    public Page<PostListResponseDto> getPosts(BoardType boardType, int pageNum) {
        return boardRepository.getPosts(boardType,pageNum);
    }

    public List<PostListResponseDto> getNoticePosts(){
        return boardRepository.getNoticePosts();
    }

    /**
     * 하나의 게시물 조회
     * @param bearerToken: Bearer 토큰
     * @param boardId: 조회할 게시물 번호
     * @return 게시물
     */
    public PostDetailsResponseDto getPost(String bearerToken, Long boardId){
        String userId = jwtProvider.getUserEmailByToken(bearerToken); // 사용자 아이디 받기
        List<MemberRole> roles = jwtProvider.getUserRolesByToken(bearerToken); // 사용자 권한 받기

        PostDetailsResponseDto postDetails = boardRepository.getPostDetails(boardId);
        if(postDetails.isSecret()) { // 게시물이 비밀글일 경우
            if (postDetails.getUserId().equals(userId) || roles.contains(MemberRole.ADMIN))
                return postDetails; // 게시물의 작성자가 해당 열람요청자와 같거나, 관리자 권한을 가지고 있을경우 허용
            else
                throw new UserDefineException("해당 게시물을 열람하실 수 없습니다.", HttpStatus.UNAUTHORIZED);
        }
        return postDetails;
    }

    /**
     * 게시물 저장
     * @param bearerToken: Bearer 토큰
     * @param requestDto: 저장할 게시물의 정보
     */
    public void saveBoard(String bearerToken, PostDetailsRequestDto requestDto) {
        String userId = jwtProvider.getUserEmailByToken(bearerToken);
        if(requestDto.isNotice() && !jwtProvider.getUserRolesByToken(bearerToken).contains(MemberRole.ADMIN))
            throw new UserDefineException("해당 게시물을 공지사항할 수 있는 권한이 없습니다.", HttpStatus.UNAUTHORIZED);

        System.out.println(requestDto.isNotice());
        System.out.println(requestDto.isSecret());
        boardRepository.save(requestDto.toEntity(signService.findById(userId)));
    }

    /**
     * 게시물 수정
     * @param bearerToken
     * @param updatePostDto
     */
    public void updateBoard(String bearerToken,PostDetailsRequestDto updatePostDto) {
        String userId = jwtProvider.getUserEmailByToken(bearerToken);
        Board board = findBoardById(updatePostDto.getBoardId());
        if(userId.equals(board.getMember().getId())  // 작성자 또는 관리자의 권한이 있어야한다.
                || jwtProvider.getUserRolesByToken(bearerToken).contains(MemberRole.ADMIN)) {
            board.updateBoard(updatePostDto);
            boardRepository.save(board);
        }else{
            throw new UserDefineException("해당 게시물을 수정할 수 있는 권한이 없습니다.", HttpStatus.UNAUTHORIZED);
        }
    }

    /**
     * 게시물 삭제
     * @param boardId: 삭제할 게시물 아이디
     */
    public void deleteBoard(String bearerToken,Long boardId) {
        String userId = jwtProvider.getUserEmailByToken(bearerToken);
        Board board = findBoardById(boardId);
        if(userId.equals(board.getMember().getId())
                || jwtProvider.getUserRolesByToken(bearerToken).contains(MemberRole.ADMIN)) {
            boardRepository.delete(board);
        }else{
            throw new UserDefineException("해당 게시물을 삭제할 수 있는 권한이 없습니다.", HttpStatus.UNAUTHORIZED);
        }
    }

    public void saveComment(String bearerToken, CommentSaveRequestDto comment) {
        Member member = signService.findById(jwtProvider.getUserEmailByToken(bearerToken)); //validation check
        Board board = findBoardById(Long.valueOf(comment.getBoardId()));
        commentRepository.save(comment.toEntity(member, board));
    }

    public void updateComment(String bearerToken, CommentRequestDto updateComment) {
        signService.findById(updateComment.getUserId());    //Validation Check
        Comment comment = commentRepository.findByUserIdAndUpdateTime(updateComment.getUserId(), updateComment.getUpdateTime())
                                                .orElseThrow(() -> new UserDefineException("해당 댓글이 존재하지 않습니다."));

        commentRepository.save(comment.update(updateComment.getContent()));
    }

    public void deleteComment(String bearerToken,CommentRequestDto deleteComment) {
        signService.findById(deleteComment.getUserId());    //Validation Check
        Comment comment = commentRepository.findByUserIdAndUpdateTime(deleteComment.getUserId(), deleteComment.getUpdateTime())
                .orElseThrow(() -> new UserDefineException("해당 댓글이 존재하지 않습니다."));
        commentRepository.delete(comment);
    }
}