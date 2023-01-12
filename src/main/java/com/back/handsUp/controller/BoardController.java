package com.back.handsUp.controller;

import com.back.handsUp.baseResponse.BaseResponse;
import com.back.handsUp.domain.board.Board;
import com.back.handsUp.service.BoardService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequestMapping("/boards")
@RestController
public class BoardController {

    @Autowired
    private BoardService boardService;

    @GetMapping("/test")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("Controller Works");
    }

    @GetMapping("/singleList/{boardIdx}")
    public BaseResponse<Board> BoardViewByIdx(@PathVariable("boardIdx") int boardIdx) {
        Board board = boardService.boardViewByIdx(boardIdx);
        return new BaseResponse<>(board);
    }

    @PostMapping("/like/{boardIdx}")
    public BaseResponse<String> like(@PathVariable("boardIdx") int boardIdx, @RequestBody int userIdx) {
        String str = "하트 누름";
        return new BaseResponse<>(str);
    }
}
