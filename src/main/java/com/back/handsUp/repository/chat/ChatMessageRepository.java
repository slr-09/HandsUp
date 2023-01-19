package com.back.handsUp.repository.chat;

import com.back.handsUp.domain.chat.ChatMessage;
import com.back.handsUp.domain.chat.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    List<ChatMessage> findByChatRoomIdxOrderByChatMessageIdxDesc(ChatRoom chatRoomIdx);
    //Todo: 채팅 조회 동적 페이지네이션 적용
//    List<ChatMessage> findByChatMessageIdxLessThanAndChatRoomIdxAndUserIdxInOrderByChatMessageIdxDesc(Long lastChatMessageIdx,Long chatRoomIdx);

}
