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
import com.back.handsUp.repository.board.BoardRepository;
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
import java.util.ArrayList;
import java.util.List;
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
    private final BoardRepository boardRepository;
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
            character = chatRoom.getHostUserIdx().getCharacter();
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



        Optional<User> optionalMe = userRepository.findByEmailAndStatus(principal.getName(), "ACTIVE");

        if (optionalMe.isEmpty()) {
            throw new BaseException(BaseResponseStatus.NON_EXIST_EMAIL);
        }
        User me = optionalMe.get();

        Optional<User> optionalUser = userRepository.findByEmailAndStatus(email.getEmail(), "ACTIVE");

        if (optionalUser.isEmpty()) {

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
            throw new BaseException(BaseResponseStatus.PUSH_NOTIFICATION_SEND_ERROR);
        }
    }

    //채팅방 개설
    public String createChat(Principal principal, ChatDto.ReqCreateChat reqCreateChat) throws BaseException{
        if (this.chatRoomRepository.findChatRoomByChatRoomKey(reqCreateChat.getChatRoomKey()).isPresent()) {
            throw new BaseException(EXIST_CHATROOMKEY);
        }

        Optional<User> optionalMe = userRepository.findByEmailAndStatus(principal.getName(), "ACTIVE");
        if (optionalMe.isEmpty()) {
            throw new BaseException(BaseResponseStatus.NON_EXIST_EMAIL);
        }
        //채팅방을 개설할 때는 사용자가 게시글 작성자에게 채팅을 보냄 -> 개설자는 항상 참여자, 작성자는 항상 호스트.
        User subUser = optionalMe.get();

        Long boardIdx = reqCreateChat.getBoardIdx();

        Optional<Board> optionalBoard = this.boardRepository.findByBoardIdx(boardIdx);
        if (optionalBoard.isEmpty()) {
            throw new BaseException(NON_EXIST_BOARDIDX);
        }
        Board board = optionalBoard.get();

        Optional<User> optionalHostUser = this.boardUserRepository.findUserIdxByBoardIdxAndStatus(boardIdx, "WRITE");
        if (optionalHostUser.isEmpty()) {
            throw new BaseException(NON_EXIST_USERIDX);
        }
        User hostUser = optionalHostUser.get();

        ChatRoom chatRoom = ChatRoom.builder()
                .boardIdx(board)
                .subUserIdx(subUser)
                .hostUserIdx(hostUser)
                .chatRoomKey(reqCreateChat.getChatRoomKey()).build();

        try {
            this.chatRoomRepository.save(chatRoom);
            return "채팅방을 개설하였습니다.";
        } catch (Exception e) {
            throw new BaseException(DATABASE_INSERT_ERROR);
        }
    }

    public List<ChatDto.ResChatList> viewAllList(Principal principal) throws BaseException {
        Optional<User> optionalUser = userRepository.findByEmailAndStatus(principal.getName(), "ACTIVE");
        if (optionalUser.isEmpty()) {
            throw new BaseException(NON_EXIST_USERIDX);
        }
        User user = optionalUser.get();

        List<ChatRoom> allChatRoom = this.chatRoomRepository.findChatRoomByUserIdx(user);

        List<ChatDto.ResChatList> chatList = new ArrayList<>();

        for (ChatRoom chat : allChatRoom) {
            ChatDto.ResChatList resChat = new ChatDto.ResChatList();
            if (chat.getHostUserIdx() == user) { //내가 호스트인 경우 참여자 정보 보내줌
                resChat.setCharacter(chat.getSubUserIdx().getCharacter());
                resChat.setNickname(chat.getSubUserIdx().getNickname());
            } else if (chat.getSubUserIdx() == user) { //내가 참여자인 경우 호스트 정보 보내줌
                resChat.setCharacter(chat.getHostUserIdx().getCharacter());
                resChat.setNickname(chat.getHostUserIdx().getNickname());
            } else continue;
            resChat.setChatRoomIdx(chat.getChatRoomIdx());
            resChat.setChatRoomKey(chat.getChatRoomKey());
            chatList.add(resChat);
        }

        return chatList;
    }
}
