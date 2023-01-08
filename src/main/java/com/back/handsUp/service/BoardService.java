package com.back.handsUp.service;

import com.back.handsUp.domain.board.Board;
import com.back.handsUp.repository.BoardRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class BoardService {
    private final BoardRepository boardRepository;

    public BoardService(BoardRepository boardRepository) {
        this.boardRepository = boardRepository;
    }


    public Board boardViewByIdx(int idx) {
        Board board = this.boardRepository.boardViewByIdx(idx);
        log.info("board ={}", board);
        return board;
    }
}
