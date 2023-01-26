package com.back.handsUp.service;

import static com.back.handsUp.baseResponse.BaseResponseStatus.*;

import com.back.handsUp.baseResponse.BaseException;
import com.back.handsUp.baseResponse.BaseResponseStatus;
import com.back.handsUp.domain.board.Board;
import com.back.handsUp.domain.board.BoardUser;
import com.back.handsUp.domain.chat.ChatRoom;
import com.back.handsUp.domain.user.Character;
import com.back.handsUp.domain.user.User;
import com.back.handsUp.dto.chat.ChatDto;
import com.back.handsUp.repository.board.BoardUserRepository;
import com.back.handsUp.repository.chat.ChatRoomRepository;
import com.back.handsUp.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.security.Principal;
import java.util.Objects;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class ChatService {
    private final ChatRoomRepository chatRoomRepository;
    private final UserRepository userRepository;
    private final BoardUserRepository boardUserRepository;
    private final BoardService boardService;
    //채팅방 조회
    public ChatDto.ResChat getChatInfo(Principal principal, Long chatRoomIdx) throws BaseException {
        Optional<User> optional = this.userRepository.findByEmail(principal.getName());
        if(optional.isEmpty()){
            throw new BaseException(BaseResponseStatus.NON_EXIST_EMAIL);
        }
        User loginUser = optional.get();

        Optional<ChatRoom> optional1 = this.chatRoomRepository.findByChatRoomIdx(chatRoomIdx);
        if(optional1.isEmpty()){
            throw new BaseException(BaseResponseStatus.NON_EXIST_CHATROOMIDX);
        }
        ChatRoom chatRoom = optional1.get();

        Optional<BoardUser> optional2 = this.boardUserRepository.findBoardUserByBoardIdx(chatRoom.getBoardIdx());
        if(optional2.isEmpty()){
            throw new BaseException(BaseResponseStatus.NON_EXIST_BOARDUSERIDX);
        }
        BoardUser boardUser = optional2.get();

        Character character;
        if(loginUser.equals(boardUser.getUserIdx())){
            character = chatRoom.getUserIdx().getCharacter();
        } else {
            character = boardUser.getUserIdx().getCharacter();
        }

        ChatDto.ResChat resChat = ChatDto.ResChat.builder()
                .board(chatRoom.getBoardIdx())
                .character(character)
                .nickname(boardUser.getUserIdx().getNickname())
                .build();

        return resChat;
    }

    public String blockChatAndBoards(Principal principal, Long chatRoomIdx) throws BaseException {
        Optional<ChatRoom> optionalChatRoom = chatRoomRepository.findByChatRoomIdx(chatRoomIdx);
        if(optionalChatRoom.isEmpty()){
            throw new BaseException(BaseResponseStatus.NON_EXIST_CHATROOMIDX);
        }

        ChatRoom chatRoom = optionalChatRoom.get();
        chatRoom.setStatus("INACTIVE");
        String result = "채팅방을 차단하였습니다.\n";
        Board board = chatRoom.getBoardIdx();
        result += boardService.blockBoard(principal, board.getBoardIdx());
        return result;
    }
}
