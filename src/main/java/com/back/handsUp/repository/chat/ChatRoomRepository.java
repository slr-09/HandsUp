package com.back.handsUp.repository.chat;

import com.back.handsUp.domain.board.Board;
import com.back.handsUp.domain.chat.ChatRoom;
import com.back.handsUp.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    Optional<ChatRoom> findByChatRoomIdx(Long chatRoomIdx);
    Optional<ChatRoom> findChatRoomByBoardIdxAndSubUserIdx(Board boardIdx, User userIdx);

    @Query("select c from ChatRoom c where (c.hostUserIdx = ?1 or c.subUserIdx= ?1) and c.status = 'ACTIVE'")
    List<ChatRoom> findChatRoomByUserIdx(User userIdx);

    Optional<ChatRoom> findChatRoomByChatRoomKey(String chatRoomKey);
}
