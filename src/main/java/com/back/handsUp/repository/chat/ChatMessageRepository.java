package com.back.handsUp.repository.chat;

import com.back.handsUp.domain.chat.ChatMessage;
import com.back.handsUp.domain.chat.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
}
