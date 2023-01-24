package com.back.handsUp.domain.chat;

import com.back.handsUp.baseResponse.BaseEntity;
import com.back.handsUp.domain.board.Board;
import com.back.handsUp.domain.user.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;

@Entity
@Setter
@Getter
@Table(name = "chat_room")
@NoArgsConstructor
@DynamicInsert
public class ChatRoom extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long chatRoomIdx;

    @OneToOne
    @JoinColumn(name = "boardIdx")
    private Board boardIdx;

    @OneToOne
    @JoinColumn(name = "userIdx")
    private User userIdx;

    @Column(columnDefinition = "varchar(10) default 'ACTIVE'")
    private String status;

    @Builder
    public ChatRoom(Board boardIdx, User userIdx) {
        this.boardIdx = boardIdx;
        this.userIdx = userIdx;
    }
}
