package com.back.handsUp.service;

import static com.back.handsUp.baseResponse.BaseResponseStatus.*;

import com.back.handsUp.baseResponse.BaseException;
import com.back.handsUp.baseResponse.BaseResponseStatus;
import com.back.handsUp.domain.board.Board;
import com.back.handsUp.domain.board.BoardUser;
import com.back.handsUp.domain.chat.ChatRoom;
import com.back.handsUp.domain.fcmToken.FcmToken;
import com.back.handsUp.domain.user.Character;
import com.back.handsUp.domain.user.User;
import com.back.handsUp.dto.chat.ChatDto;
import com.back.handsUp.dto.user.UserDto;
import com.back.handsUp.repository.board.BoardUserRepository;
import com.back.handsUp.repository.chat.ChatRoomRepository;
import com.back.handsUp.repository.fcm.FcmTokenRepository;
import com.back.handsUp.repository.user.UserRepository;
import com.back.handsUp.utils.FirebaseCloudMessageService;
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
    private final FcmTokenRepository fcmTokenRepository;
    private final FirebaseCloudMessageService firebaseCloudMessageService;
    //채팅방 조회
    public ChatDto.ResChat getChatInfo(Principal principal, Long chatRoomIdx) throws BaseException {
        Optional<User> optional = this.userRepository.findByEmailAndStatus(principal.getName(), "ACTIVE");
        if(optional.isEmpty()){
            throw new BaseException(BaseResponseStatus.NON_EXIST_EMAIL);
        }
        User loginUser = optional.get();

        Optional<ChatRoom> optional1 = this.chatRoomRepository.findByChatRoomIdx(chatRoomIdx);
        if(optional1.isEmpty()){
            throw new BaseException(BaseResponseStatus.NON_EXIST_CHATROOMIDX);
        }
        ChatRoom chatRoom = optional1.get();

        Optional<BoardUser> optional2 = this.boardUserRepository.findBoardUserByBoardIdxAndStatus(chatRoom.getBoardIdx(), "WRITE")
                .stream().findFirst();
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

    public String chatAlarm(Principal principal, UserDto.ResEmail email) throws BaseException {

        System.out.println("--------------------------------");
        System.out.println("--------------------------------");
        System.out.println(email.getEmail());
        System.out.println("--------------------------------");
        System.out.println("--------------------------------");

        Optional<User> optionalMe = userRepository.findByEmailAndStatus(principal.getName(), "ACTIVE");

        if (optionalMe.isEmpty()) {
            System.out.println("--------------------------------");
            System.out.println("--------------------------------");
            System.out.println("내 이메일 검색");
            System.out.println("--------------------------------");
            System.out.println("--------------------------------");
            throw new BaseException(BaseResponseStatus.NON_EXIST_EMAIL);
        }
        User me = optionalMe.get();

        Optional<User> optionalUser = userRepository.findByEmailAndStatus(email.getEmail(), "ACTIVE");

        if (optionalUser.isEmpty()) {
            System.out.println("--------------------------------");
            System.out.println("--------------------------------");
            System.out.println("니 이메일 검색");
            System.out.println("--------------------------------");
            System.out.println("--------------------------------");
            throw new BaseException(BaseResponseStatus.NON_EXIST_EMAIL);
        }
        User you = optionalUser.get();
        Optional<FcmToken> optionalFcmToken = fcmTokenRepository.findFcmTokenByUser(you);
        if (optionalFcmToken.isEmpty()) {
            throw new BaseException(BaseResponseStatus.NON_EXIST_FCMTOKEN);
        }
        FcmToken fcmToken = optionalFcmToken.get();

        try {
            firebaseCloudMessageService.sendMessageTo(fcmToken.getFcmToken(), me.getNickname(), "채팅이 도착하였습니다.");
            return "채팅 알림을 성공적으로 보냈습니다.";
        } catch (Exception e) {
            System.out.println("-----------------");
            System.out.println("-----------------");
            System.out.println("SEND ERROR");
            System.out.println(e.getMessage());
            System.out.println("-----------------");
            System.out.println("-----------------");
            throw new BaseException(BaseResponseStatus.PUSH_NOTIFICATION_SEND_ERROR);
        }
    }
}
