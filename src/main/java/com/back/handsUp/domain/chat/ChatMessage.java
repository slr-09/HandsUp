package com.back.handsUp.domain.chat;

import com.back.handsUp.baseResponse.BaseEntity;
import com.back.handsUp.domain.user.User;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;

@Entity
@Setter
@Getter
@Table(name = "chat_message")
@NoArgsConstructor
@DynamicInsert
public class ChatMessage extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long chatMessageIdx;

    @Column(nullable = false)
    private String chatContents;

    @ManyToOne
    @JoinColumn(name = "chatRoomIdx")
    private ChatRoom chatRoomIdx;

    @OneToOne
    @JoinColumn(name = "userIdx")
    private User userIdx;

    @Column(columnDefinition = "varchar(10) default 'ACTIVE'")
    private String status;

    @Builder
    public ChatMessage(String chatContents, ChatRoom chatRoomIdx, User userIdx) {
        this.chatContents = chatContents;
        this.chatRoomIdx = chatRoomIdx;
        this.userIdx = userIdx;
    }
}
