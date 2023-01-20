package com.back.handsUp.service;

import com.back.handsUp.baseResponse.BaseException;
import com.back.handsUp.baseResponse.BaseResponseStatus;
import com.back.handsUp.domain.board.*;
import com.back.handsUp.domain.user.User;
import com.back.handsUp.dto.board.BoardDto;
import com.back.handsUp.dto.board.BoardPreviewRes;
import com.back.handsUp.repository.board.BoardRepository;
import com.back.handsUp.repository.board.BoardTagRepository;
import com.back.handsUp.repository.board.BoardUserRepository;
import com.back.handsUp.repository.board.TagRepository;
import com.back.handsUp.repository.user.UserRepository;
import com.back.handsUp.utils.FirebaseCloudMessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import javax.transaction.Transactional;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import static com.back.handsUp.baseResponse.BaseResponseStatus.DATABASE_INSERT_ERROR;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class BoardService {
    private final BoardRepository boardRepository;
    private final TagRepository tagRepository;
    private final BoardTagRepository boardTagRepository;
    private final UserRepository userRepository;
    private final BoardUserRepository boardUserRepository;
    private final FirebaseCloudMessageService firebaseCloudMessageService;


    public Board boardViewByIdx(Long boardIdx) throws BaseException {
        Optional<Board> optional = this.boardRepository.findByBoardIdx(boardIdx);
        if(optional.isEmpty()){
            throw new BaseException(BaseResponseStatus.NON_EXIST_BOARDIDX);
        }
        return optional.get();
    }

    //전체 게시물 조회
    public List<Board> showBoardList() throws BaseException {
//        long boardNum = boardRepository.count();
//        if (boardNum==0){
//            throw new BaseException(BaseResponseStatus.NON_EXIST_BOARD_LIST);
//        }
        try {
            List<Board> getBoards = boardRepository.findBoardByStatus("ACTIVE");

            return getBoards;
        } catch (Exception exception) {
            throw new BaseException(BaseResponseStatus.DATABASE_INSERT_ERROR);
        }

    }

    //전체 게시물(지도 상) 조회
    // 캐릭터, 위치(Board), boardIdx
    public List<BoardDto.GetBoardMap> showBoardMapList() throws BaseException {

        List<Board> getBoards = boardRepository.findBoardByStatus("ACTIVE");

        List<BoardDto.GetBoardMap> getBoardsMapList = new ArrayList<>();


        for(Board b : getBoards) {
            Optional<BoardUser> optional = this.boardUserRepository.findBoardUserByBoardIdx(b);

            BoardUser boardUser = optional.get();

            BoardDto.GetBoardMap getBoardMap = BoardDto.GetBoardMap.builder()
                    .boardIdx(b.getBoardIdx())
                    .character(boardUser.getUserIdx().getCharacterIdx())
                    .location(b.getLocation())
                    .build();

            getBoardsMapList.add(getBoardMap);
        }


        return getBoardsMapList;
    }

    public void likeBoard(Principal principal, Long boardIdx) throws BaseException {

        Optional<Board> optionalBoard = boardRepository.findByBoardIdx(boardIdx);

        if (optionalBoard.isEmpty()) {
            throw new BaseException(BaseResponseStatus.NON_EXIST_BOARD_LIST);
        }
        Board board = optionalBoard.get();

        //user : 하트 누르는 사용자.
        Optional<User> optionalUser = userRepository.findByEmail(principal.getName());

        if (optionalUser.isEmpty()) {
            throw new BaseException(BaseResponseStatus.NON_EXIST_EMAIL);
        }
        User user = optionalUser.get();

        //boardUser : 게시글 작성자.
        Optional<User> optionalBoardUser = boardUserRepository.findUserIdxByBoardIdxAndStatus(boardIdx, "WRITE");

        if (optionalBoardUser.isEmpty()) {
            throw new BaseException(BaseResponseStatus.NON_EXIST_EMAIL);
        }
        User boardUser = optionalBoardUser.get();

        BoardUser likeEntity = BoardUser.builder()
                .userIdx(user)
                .boardIdx(board)
                .status("LIKE").build();

        try {
            boardUserRepository.save(likeEntity);
        } catch (Exception e) {
            throw new BaseException(DATABASE_INSERT_ERROR);
        }

//Todo : User FcmToken 추가 후 주석 해제.
//      하트 알림 전송 부분.
//        if (!Objects.equals(user.getUserIdx(), boardUser.getUserIdx())) {
//                firebaseCloudMessageService.sendMessageTo(boardUser.getFcmToken(), "Hands Up", "회원님의 핸즈업에 누군가 하트를 눌렀습니다.");
//        }


    }

    public List<BoardPreviewRes> viewMyBoard(Principal principal) throws BaseException{
        //long myIdx = 1L; // = jwtService.getUserIdx(token);
        log.info("principal.getName() = {}",principal.getName());
        Optional<User> optionalUser = userRepository.findByEmail(principal.getName());

        if (optionalUser.isEmpty()) {
            throw new BaseException(BaseResponseStatus.NON_EXIST_EMAIL);
        }
        User user = optionalUser.get();

        log.info("start boardUser");

        List<Board> boardUser = boardUserRepository.findBoardIdxByUserIdxAndStatus(user, "WRITE");

        log.info("boardUserIdx = {}", boardUser);

        return boardUser.stream()
                .map(Board::toPreviewRes)
                .collect(Collectors.toList());
    }

    public void addBoard(Principal principal, BoardDto.GetBoardInfo boardInfo) throws BaseException {

        if(boardInfo.getIndicateLocation().equals("true") && boardInfo.getLocation() == null){
            throw new BaseException(BaseResponseStatus.LOCATION_ERROR);
        }

        if(boardInfo.getMessageDuration()<1 || boardInfo.getMessageDuration()>48){
            throw new BaseException(BaseResponseStatus.MESSAGEDURATION_ERROR);
        }

        Board boardEntity = Board.builder()
                .content(boardInfo.getContent())
                .indicateLocation(boardInfo.getIndicateLocation())
                .location(boardInfo.getLocation())
                .messageDuration(boardInfo.getMessageDuration())
                .build();
        try{
            this.boardRepository.save(boardEntity);
            setTags(boardInfo, boardEntity);
        } catch (Exception e) {
            throw new BaseException(DATABASE_INSERT_ERROR);
        }

        Optional<User> optional = this.userRepository.findByEmail(principal.getName());
        if(optional.isEmpty()){
            throw new BaseException(BaseResponseStatus.NON_EXIST_EMAIL);
        }
        User userEntity = optional.get();
        BoardUser boardUserEntity = BoardUser.builder()
                .boardIdx(boardEntity)
                .userIdx(userEntity)
                .status("WRITE")
                .build();
        try{
            this.boardUserRepository.save(boardUserEntity);
        } catch (Exception e) {
            throw new BaseException(DATABASE_INSERT_ERROR);
        }

    }

    public void patchBoard(Principal principal, Long boardIdx, BoardDto.GetBoardInfo boardInfo) throws BaseException{
        if(boardInfo.getIndicateLocation().equals("true") && boardInfo.getLocation() == null){
            throw new BaseException(BaseResponseStatus.LOCATION_ERROR);
        }

        if(boardInfo.getMessageDuration()<1 || boardInfo.getMessageDuration()>48){
            throw new BaseException(BaseResponseStatus.MESSAGEDURATION_ERROR);
        }

        Optional<Board> optional = this.boardRepository.findByBoardIdx(boardIdx);
        if(optional.isEmpty()){
            throw new BaseException(BaseResponseStatus.NON_EXIST_BOARDIDX);
        }
        Board boardEntity = optional.get();


        Optional<User> optional1 = this.userRepository.findByEmail(principal.getName());
        if(optional1.isEmpty()){
            throw new BaseException(BaseResponseStatus.NON_EXIST_EMAIL);
        }
        User userEntity = optional1.get();

        Optional<BoardUser> optionalBoardUser = this.boardUserRepository.findBoardUserByBoardIdxAndUserIdx(boardEntity, userEntity);
        if(optionalBoardUser.isEmpty()){
            throw new BaseException(BaseResponseStatus.NON_EXIST_BOARDUSERIDX);
        }

        boardEntity.changeBoard(boardInfo.getContent(), boardInfo.getLocation(), boardInfo.getIndicateLocation(), boardInfo.getMessageDuration());
        try{
            this.boardRepository.save(boardEntity);
        } catch (Exception e) {
            throw new BaseException(DATABASE_INSERT_ERROR);
        }

        List<BoardTag> boardTagEntityList = this.boardTagRepository.findAllByBoardIdx(boardEntity);
        for(BoardTag boardTag : boardTagEntityList){
            boardTag.changeStatus("INACTIVE");
        }
        try{
            setTags(boardInfo, boardEntity);
        } catch (Exception e) {
            throw new BaseException(DATABASE_INSERT_ERROR);
        }
    }

    private void setTags(BoardDto.GetBoardInfo boardInfo, Board boardEntity) {
            Optional<Tag> tagEntity = this.tagRepository.findByName(boardInfo.getTag());
            Tag targetTag;

            if(tagEntity.isEmpty()){
                targetTag = Tag.builder()
                        .name(boardInfo.getTag())
                        .build();
                this.tagRepository.save(targetTag);
            } else {
               targetTag = tagEntity.get();
            }

            Optional<BoardTag> optional = this.boardTagRepository.findByBoardIdxAndTagIdx(boardEntity, targetTag);
            if(optional.isEmpty()){
                BoardTag boardTagEntity = BoardTag.builder()
                        .boardIdx(boardEntity)
                        .tagIdx(targetTag)
                        .build();
                this.boardTagRepository.save(boardTagEntity);
            } else{
                BoardTag boardTagEntity = optional.get();
                boardTagEntity.changeStatus("ACTIVE");
            }
    }
}
