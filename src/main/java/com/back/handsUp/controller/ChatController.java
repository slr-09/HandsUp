package com.back.handsUp.controller;

import com.back.handsUp.baseResponse.BaseException;
import com.back.handsUp.baseResponse.BaseResponse;
import com.back.handsUp.domain.board.Board;
import com.back.handsUp.domain.chat.ChatMessage;
import com.back.handsUp.dto.chat.ChatDto;
import com.back.handsUp.dto.user.UserDto;
import com.back.handsUp.service.BoardService;
import com.back.handsUp.service.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/chats")
@RestController
public class ChatController {
    private final ChatService chatService;
    private final BoardService boardService;
    //채팅 메세지 조회
    @ResponseBody
    @GetMapping("/{chatRoomIdx}")
    public BaseResponse<ChatDto.ResChat> getChatMessages(Principal principal, @PathVariable Long chatRoomIdx){
        try {
            ChatDto.ResChat resChat = this.chatService.getChatInfo(principal,chatRoomIdx);
            return new BaseResponse<>(resChat);
        }catch (BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }


    @PostMapping("/block/{chatRoomIdx}")
    public BaseResponse<String> blockChatAndBoards(Principal principal, @PathVariable Long chatRoomIdx) {

        try {
            String result = chatService.blockChatAndBoards(principal, chatRoomIdx);

            return new BaseResponse<>(result);
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    @PostMapping("/alarm")
    public BaseResponse<String> chatAlarm(Principal principal, @RequestBody UserDto.ResEmail email) {

        try {
            String result = chatService.chatAlarm(principal, email);

            return new BaseResponse<>(result);
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }
}
