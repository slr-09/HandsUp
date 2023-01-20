package com.back.handsUp.service;

import com.back.handsUp.baseResponse.BaseException;
import com.back.handsUp.baseResponse.BaseResponseStatus;
import com.back.handsUp.domain.board.BoardUser;
import com.back.handsUp.domain.chat.ChatMessage;
import com.back.handsUp.domain.chat.ChatRoom;
import com.back.handsUp.domain.user.User;
import com.back.handsUp.dto.chat.ChatDto;
import com.back.handsUp.repository.board.BoardUserRepository;
import com.back.handsUp.repository.chat.ChatMessageRepository;
import com.back.handsUp.repository.chat.ChatRoomRepository;
import com.back.handsUp.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class ChatService {
    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final UserRepository userRepository;
    private final BoardUserRepository boardUserRepository;

    //채팅 메세지 조회
    public ChatDto.ResChatMessageList getChatMessages(Principal principal, Long chatRoomIdx) throws BaseException {
        Optional<User> optional = this.userRepository.findByEmail(principal.getName());
        if(optional.isEmpty()){
            throw new BaseException(BaseResponseStatus.NON_EXIST_EMAIL);
        }

        Optional<ChatRoom> optional1 = this.chatRoomRepository.findByChatRoomIdx(chatRoomIdx);
        if(optional1.isEmpty()){
            throw new BaseException(BaseResponseStatus.NON_EXIST_CHATROOMIDX);
        }
        ChatRoom chatRoom = optional1.get();
        List<ChatMessage> chatMessageList = this.chatMessageRepository.findByChatRoomIdxOrderByChatMessageIdxDesc(chatRoom);
        List<ChatDto.BriefChatMessage> briefChatMessageList = new ArrayList<>();
        for(ChatMessage chat: chatMessageList){
            Boolean isMe;
            if(chat.getUserIdx().equals(optional.get())){
                isMe=Boolean.TRUE;
            } else{
                isMe=Boolean.FALSE;
            }
            ChatDto.BriefChatMessage briefChatMessage = ChatDto.BriefChatMessage.builder()
                    .chatMessageIdx(chat.getChatMessageIdx())
                    .isMe(isMe)
                    .chatContents(chat.getChatContents())
                    .createdAt(chat.getCreatedAt())
                    .build();

            briefChatMessageList.add(briefChatMessage);
        }

        Optional<BoardUser> optional2 = this.boardUserRepository.findBoardUserByBoardIdx(chatRoom.getBoardIdx());
        if(optional2.isEmpty()){
            throw new BaseException(BaseResponseStatus.NON_EXIST_BOARDUSERIDX);
        }
        BoardUser boardUser = optional2.get();


        ChatDto.ResChatMessageList resChatMessageList =ChatDto.ResChatMessageList.builder()
                .board(chatRoom.getBoardIdx())
                .character(boardUser.getUserIdx().getCharacterIdx())
                .nickname(boardUser.getUserIdx().getNickname())
                .chatMessageList(briefChatMessageList)
                .build();

        return resChatMessageList;
    }
}
