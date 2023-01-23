package com.back.handsUp.repository.chat;

import com.back.handsUp.domain.board.Board;
import com.back.handsUp.domain.chat.ChatRoom;
import com.back.handsUp.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    Optional<ChatRoom> findByChatRoomIdx(Long chatRoomIdx);
    Optional<ChatRoom> findChatRoomByBoardIdxAndUserIdx(Board boardIdx, User userIdx);
}
