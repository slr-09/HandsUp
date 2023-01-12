package com.back.handsUp.service;

import com.back.handsUp.domain.board.Board;
import com.back.handsUp.repository.BoardRepository;
import com.back.handsUp.utils.FirebaseCloudMessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class BoardService {
    private final BoardRepository boardRepository;
    private final FirebaseCloudMessageService firebaseCloudMessageService;

    public BoardService(BoardRepository boardRepository, FirebaseCloudMessageService firebaseCloudMessageService) {
        this.boardRepository = boardRepository;
        this.firebaseCloudMessageService = firebaseCloudMessageService;
    }


    public Board boardViewByIdx(int idx) {
        Board board = this.boardRepository.boardViewByIdx(idx);
        log.info("board ={}", board);
        return board;
    }

    public void likeBoard(int userIdx, int boardIdx) {
        User hostUser = boardRepository.findUserByBoardIdx(boardIdx);

        if (userIdx != hostUser.getUserIdx()) {

            firebaseCloudMessageService.sendMessageTo(hostUser.getFcmToken(), "Hands Up", "회원님의 핸즈업에 누군가 하트를 눌렀습니다.");
        }
    }
}
