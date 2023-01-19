package com.back.handsUp.repository.chat;

import com.back.handsUp.domain.chat.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    Optional<ChatRoom> findByChatRoomIdx(Long chatRoomIdx);
}
