package com.back.handsUp.controller;

import com.back.handsUp.baseResponse.BaseException;
import com.back.handsUp.baseResponse.BaseResponse;
import com.back.handsUp.domain.board.Board;
import com.back.handsUp.dto.board.BoardDto;
import com.back.handsUp.dto.board.BoardPreviewRes;
import com.back.handsUp.service.BoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/boards")
@RestController
public class BoardController {

    private final BoardService boardService;

    @GetMapping("/test")
    public ResponseEntity<String> test(Principal principal) {
        String str = principal.getName();
        return ResponseEntity.ok(str);
    }


    @GetMapping("/singleList/{boardIdx}")
    public BaseResponse<BoardDto.SingleBoardRes> BoardViewByIdx(Principal principal, @PathVariable("boardIdx") Long boardIdx) throws BaseException {
        BoardDto.SingleBoardRes board = boardService.boardViewByIdx(principal, boardIdx);
        return new BaseResponse<>(board);
    }

    //전체 게시물 조회
    @ResponseBody
    @GetMapping("/showList")
    public BaseResponse<List<Board>> showBoardList(Principal principal){
        try {
            List<Board> getBoards = boardService.showBoardList(principal);
            return new BaseResponse<>(getBoards);
        }catch (BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    //전체 게시물(지도 상) 조회
    @ResponseBody
    @GetMapping("/showMapList")
    public BaseResponse<List<BoardDto.GetBoardMap>> showBoardMapList(Principal principal){
        try {
            List<BoardDto.GetBoardMap> getBoardsMap = boardService.showBoardMapList(principal);
            return new BaseResponse<>(getBoardsMap);
        }catch (BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    @PostMapping("/{boardIdx}/like")
    public BaseResponse<String> like(Principal principal,@PathVariable Long boardIdx) {
        try{
            this.boardService.likeBoard(principal, boardIdx);
            return new BaseResponse<>("해당 게시글에 하트를 눌렀습니다.");
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    //본인 게시글 조회
    @GetMapping("/myBoards")
    public BaseResponse<BoardDto.MyBoard> viewMyBoard(Principal principal) {
        try {
            BoardDto.MyBoard myBoards = boardService.viewMyBoard(principal);
            return new BaseResponse<>(myBoards);
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

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
    @PostMapping("/delete/{boardIdx}")
    public BaseResponse<String> deleteBoard(Principal principal, @PathVariable Long boardIdx){
        try{
            this.boardService.deleteBoard(principal, boardIdx);
            return new BaseResponse<>("게시글을 삭제하였습니다.");
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

    @PostMapping("/block/{boardIdx}")
    public BaseResponse<String> blockBoard(Principal principal, @PathVariable Long boardIdx) {
        try {
            String result = this.boardService.blockBoard(principal, boardIdx);
            return new BaseResponse<>(result);
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    @ResponseBody
    @GetMapping("/like")
    public BaseResponse<List<BoardDto.ReceivedLikeRes>> getReceivedLikeList(Principal principal){
        try{
            List<BoardDto.ReceivedLikeRes>  receivedLikeResList= this.boardService.receivedLikeList(principal);
            return new BaseResponse<>(receivedLikeResList);
        } catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }
    }
}
