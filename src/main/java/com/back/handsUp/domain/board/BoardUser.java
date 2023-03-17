package com.back.handsUp.domain.board;

import com.back.handsUp.baseResponse.BaseEntity;
import com.back.handsUp.domain.user.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;

@Entity
@Getter
@Table(name = "board_user")
@NoArgsConstructor
@DynamicInsert
public class BoardUser extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long boardUserIdx;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "boardIdx")
    private Board boardIdx;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userIdx")
    private User userIdx;

    @Column(columnDefinition = "varchar(20) default 'WRITE'")
    private String status;

    @Builder
    public BoardUser(Board boardIdx, User userIdx, String status) {
        this.boardIdx = boardIdx;
        this.userIdx = userIdx;
        this.status = status;
    }

    public void changeStatus (String newStatus) {
        this.status = newStatus;
    }
}

