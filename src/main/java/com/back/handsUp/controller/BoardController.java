package com.back.handsUp.controller;

import com.back.handsUp.baseResponse.BaseException;
import com.back.handsUp.baseResponse.BaseResponse;
import com.back.handsUp.domain.board.Board;
import com.back.handsUp.dto.board.BoardDto;
import com.back.handsUp.service.BoardService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Slf4j
@RequestMapping("/boards")
@RestController
public class BoardController {

    @Autowired
    private BoardService boardService;

    public BoardController(BoardService boardService) {
        this.boardService = boardService;
    }

    @GetMapping("/test")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("Controller Works");
    }

    @GetMapping("/singleList/{boardIdx}")
    public BaseResponse<Board> BoardViewByIdx(@PathVariable("boardIdx") Long boardIdx) throws BaseException {
        Board board = boardService.boardViewByIdx(boardIdx);
        return new BaseResponse<>(board);
    }

//    @PostMapping("/like/{boardIdx}")
//    public BaseResponse<String> like(@PathVariable("boardIdx") Long boardIdx, @RequestBody Long userIdx) {
//        try{
//            this.boardService.likeBoard(userIdx, boardIdx);
//        } catch (BaseException e) {
//            return new BaseResponse<>(e.getStatus());
//        }
//        String str = "하트 누름";
//        return new BaseResponse<>(str);
//    }

    @ResponseBody
    @PostMapping("/")
    public BaseResponse<String> addBoard(Principal principal, @RequestBody BoardDto.GetBoardInfo boardInfo){
        try{
            this.boardService.addBoard(principal, boardInfo);
            return new BaseResponse<>("게시글을 등록하였습니다.");
        } catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }
    }

    @ResponseBody
    @PatchMapping("/{boardIdx}")
    public BaseResponse<String> patchBoard(Principal principal, @PathVariable Long boardIdx, @RequestBody BoardDto.GetBoardInfo boardInfo){
        try{
            this.boardService.patchBoard(principal, boardIdx, boardInfo);
            return new BaseResponse<>("게시글을 수정하였습니다.");
        } catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }
    }
}
