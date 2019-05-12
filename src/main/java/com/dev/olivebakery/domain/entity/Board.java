package com.dev.olivebakery.domain.entity;

import com.dev.olivebakery.domain.dto.BoardDto;
import com.dev.olivebakery.domain.enums.BoardType;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "board_tbl")
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long boardId;

    @CreationTimestamp
    private LocalDateTime insertTime;

    @UpdateTimestamp
    private LocalDateTime updateTime;

    private String title;

    @Lob
    private String context;

    // 게시판인지 QnA인지
    @Enumerated(EnumType.STRING)
    private BoardType boardType;

    //공지사항일 경우 true
    private boolean isNotice = false;

    //비밀글일 경우 true
    private boolean isSecret = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member")
    private Member member;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "board")
    private List<Comment> comments = new ArrayList<>();

    @Builder
    public Board(String title, String context, BoardType boardType, boolean isNotice, boolean isSecret, Member member) {
        this.title = title;
        this.context = context;
        this.boardType = boardType;
        this.isNotice = isNotice;
        this.isSecret = isSecret;
        this.member = member;
    }

    public void updateBoard(BoardDto.UpdatePost updatePostDto) {
        this.context = updatePostDto.getContext();
        this.title = updatePostDto.getTitle();
        this.isNotice = updatePostDto.getIsNotice().matches("true");
        this.isSecret = updatePostDto.getIsSecret().matches("true");
    }
}
