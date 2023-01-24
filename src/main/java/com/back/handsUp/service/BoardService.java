package com.back.handsUp.service;

import com.back.handsUp.baseResponse.BaseException;
import com.back.handsUp.baseResponse.BaseResponseStatus;
import com.back.handsUp.domain.board.*;
import com.back.handsUp.domain.chat.ChatRoom;
import com.back.handsUp.domain.user.Character;
import com.back.handsUp.domain.user.User;
import com.back.handsUp.dto.board.BoardDto;
import com.back.handsUp.dto.board.BoardPreviewRes;
import com.back.handsUp.dto.user.CharacterDto;
import com.back.handsUp.repository.board.BoardRepository;
import com.back.handsUp.repository.board.BoardTagRepository;
import com.back.handsUp.repository.board.BoardUserRepository;
import com.back.handsUp.repository.board.TagRepository;
import com.back.handsUp.repository.chat.ChatRoomRepository;
import com.back.handsUp.repository.user.UserRepository;
import com.back.handsUp.utils.FirebaseCloudMessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import javax.transaction.Transactional;
import java.security.Principal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
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
    private final ChatRoomRepository chatRoomRepository;


    //단일 게시물 조회
    public BoardDto.SingleBoardRes boardViewByIdx(Principal principal, Long boardIdx) throws BaseException {

        //조회하는 유저
        Optional<User> optionalUser = userRepository.findByEmail(principal.getName());

        if (optionalUser.isEmpty()) {
            throw new BaseException(BaseResponseStatus.NON_EXIST_EMAIL);
        }
        User user = optionalUser.get();

        //조회하는 게시물
        Optional<Board> optionalBoard = this.boardRepository.findByBoardIdx(boardIdx);
        if(optionalBoard.isEmpty()){
            throw new BaseException(BaseResponseStatus.NON_EXIST_BOARDIDX);
        }
        Board board = optionalBoard.get();


        String locationInfo;
        //위치 정보 동의하면 locationInfo 에 위치 담기
        if (Objects.equals(board.getIndicateLocation(), "Y")) {
            locationInfo = board.getLocation();
        } else {
            locationInfo = "위치 비밀";
        }

        //like 확인
        Optional<BoardUser> optionalBoardUserEntity = boardUserRepository.findBoardUserByBoardIdxAndUserIdx(board, user);

        String didLike;
        if(optionalBoardUserEntity.isEmpty()){
            didLike = "false";
        }else {
            BoardUser boardUserEntity = optionalBoardUserEntity.get();
            didLike = boardUserEntity.getStatus();
        }

        //like 눌렀을 때만 true 반환
        if (Objects.equals(didLike, "LIKE")) {
            didLike = "true";
        }

        //게시글 작성자
        Optional<User> optionalBoardUser = this.boardUserRepository.findUserIdxByBoardIdxAndStatus(boardIdx, "WRITE");
        if(optionalBoardUser.isEmpty()){
            throw new BaseException(BaseResponseStatus.NON_EXIST_BOARDUSERIDX);
        }
        User boardUser = optionalBoardUser.get();

        //태그
        Optional<Tag> optionalTag = this.boardTagRepository.findTagIdxByBoardIdx(boardIdx);
        if(optionalTag.isEmpty()){
            throw new BaseException(BaseResponseStatus.NON_EXIST_TAG_VALUE);
        }
        Tag tag = optionalTag.get();

        return BoardDto.SingleBoardRes.builder()
                .content(board.getContent())
                .tag(tag.getName())
                .nickname(boardUser.getNickname())
                .messageDuration(board.getMessageDuration())
                .location(locationInfo)
                .didLike(didLike)
                .createdAt(board.getCreatedAt()).build();
    }

    //전체 게시물 조회
    public List<Board> showBoardList(Principal principal) throws BaseException {

        List<Board> getBoards = getBoards(principal);

//        List<Board> getSchoolBoards = new ArrayList<>();

//        for(Board b : getStatusBoards){
//            Optional<BoardUser> optional = this.boardUserRepository.findBoardUserByBoardIdx(b);
//            if(optional.isEmpty()){
//                throw new BaseException(BaseResponseStatus.NON_EXIST_BOARDUSERIDX);
//            }
//            BoardUser boardUser = optional.get();
//            if(boardUser.getUserIdx().getSchoolIdx()==user.getSchoolIdx()){
//                getSchoolBoards.add(b);
//            }
//
//        }


        return getBoards;

    }

    //전체 게시물(지도 상) 조회
    // 캐릭터, 위치(Board), boardIdx
    public List<BoardDto.GetBoardMap> showBoardMapList(Principal principal) throws BaseException {

        List<Board> getBoards = getBoards(principal);

        List<BoardDto.GetBoardMap> getBoardsMapList = new ArrayList<>();

        for(Board b : getBoards) {
            Optional<BoardUser> optional = this.boardUserRepository.findBoardUserByBoardIdxAndStatus(b, "WRITE").stream().findFirst();
            if(optional.isEmpty()){
                throw new BaseException(BaseResponseStatus.NON_EXIST_BOARDUSERIDX);
            }
            BoardUser boardUser = optional.get();

            BoardDto.GetBoardMap getBoardMap = BoardDto.GetBoardMap.builder()
                    .boardIdx(b.getBoardIdx())
                    .character(boardUser.getUserIdx().getCharacter())
                    .location(b.getLocation())
                    .build();

            getBoardsMapList.add(getBoardMap);
        }


        return getBoardsMapList;
    }

    //게시물 조회 리스트,지도 중복 코드
    public List<Board> getBoards(Principal principal) throws BaseException {
        //조회하는 유저
        Optional<User> optionalUser = userRepository.findByEmail(principal.getName());

        if (optionalUser.isEmpty()) {
            throw new BaseException(BaseResponseStatus.NON_EXIST_EMAIL);
        }
        User user = optionalUser.get();

        List<Board> getBoards = boardUserRepository.findBoardBySchoolAndStatus(user.getSchoolIdx(), "ACTIVE");

        LocalDateTime currentTime = LocalDateTime.now();

        //시간 만료 체크
        for (Board b: getBoards){
            Duration timeCheck = Duration.between(b.getCreatedAt(),currentTime);
            if(timeCheck.getSeconds() > b.getMessageDuration() * 3600L) {
                b.changeStatus("EXPIRED");
            }
        }

        return getBoards;
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


    //게시물 삭제
    public void deleteBoard(Principal principal, Long boardIdx) throws BaseException{
        Optional<User> optional = this.userRepository.findByEmail(principal.getName());
        if(optional.isEmpty()){
            throw new BaseException(BaseResponseStatus.NON_EXIST_USERIDX);
        }
        User userEntity = optional.get();

        Optional<Board> myBoards = this.boardRepository.findByBoardIdx(boardIdx);
        if(myBoards.isEmpty()){
            throw new BaseException(BaseResponseStatus.NON_EXIST_BOARDIDX);
        }
        Board myBoardsEntity = myBoards.get();

        Optional<BoardUser> boardUser = this.boardUserRepository.findBoardUserByBoardIdxAndStatus(myBoardsEntity, "WRITE").stream().findFirst();

        //게시물이 존재하는지 체크
        if(boardUser.isEmpty()){
            throw new BaseException(BaseResponseStatus.NON_EXIST_BOARD_LIST);
        }

        //해당 게시물을 로그인한 유저가 작성했는지 체크
        if(!Objects.equals(boardUser.get().getUserIdx().getUserIdx(), userEntity.getUserIdx())){
            throw new BaseException(BaseResponseStatus.NON_EXIST_BOARDUSERIDX);
        }

        //이미 삭제된 게시물인지 체크
        if(myBoardsEntity.getStatus().equals("DELETE")){
            throw new BaseException(BaseResponseStatus.ALREADY_DELETE_BOARD);
        }


        myBoards.get().changeStatus("DELETE");


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
            Optional<Tag> tagEntity = this.tagRepository.findTagByName(boardInfo.getTag());
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
                log.info("new boardTag save done");

            } else{
                BoardTag boardTagEntity = optional.get();
                boardTagEntity.changeStatus("ACTIVE");
            }
    }

    //받은 하트 목록 조회
    public List<BoardDto.ReceivedLikeRes> receivedLikeList (Principal principal) throws BaseException {
        Optional<User> optional = this.userRepository.findByEmail(principal.getName());
        if(optional.isEmpty()){
            throw new BaseException(BaseResponseStatus.NON_EXIST_USERIDX);
        }
        User user = optional.get();

        List<Board> boardList = this.boardUserRepository.findBoardIdxByUserIdxAndStatus(user, "WRITE");
        List<BoardUser> boardUserList = new ArrayList<>();
        for (Board board : boardList){
                boardUserList.addAll(this.boardUserRepository.findBoardUserByBoardIdxAndStatus(board, "LIKE"));
            }

        List<BoardDto.ReceivedLikeRes> receivedLikeList = new ArrayList<>();
        for(BoardUser boardUser: boardUserList){
            Character character = boardUser.getUserIdx().getCharacter();
            CharacterDto.GetCharacterInfo characterInfo = new CharacterDto.GetCharacterInfo(character.getEye(),
                    character.getEyeBrow(), character.getGlasses(), character.getNose(), character.getMouth(),
                    character.getHair(), character.getHairColor(), character.getSkinColor(), character.getBackGroundColor());

            Optional<ChatRoom> chatRoomOptional = this.chatRoomRepository.findChatRoomByBoardIdxAndUserIdx(boardUser.getBoardIdx(), boardUser.getUserIdx());
            if(chatRoomOptional.isEmpty()){
                throw new BaseException(BaseResponseStatus.NON_EXIST_CHATROOMIDX);
            }

            BoardDto.ReceivedLikeRes receivedLike = BoardDto.ReceivedLikeRes.builder()
                    .text("아래 글에 "+boardUser.getUserIdx().getNickname()+"님이 관심있어요")
                    .boardContent(boardUser.getBoardIdx().getContent())
                    .LikeCreatedAt(boardUser.getCreatedAt())
                    .character(characterInfo)
                    .chatRoomIdx(chatRoomOptional.get().getChatRoomIdx())
                    .build();
            receivedLikeList.add(receivedLike);
        }
        receivedLikeList.sort(Collections.reverseOrder());
        return receivedLikeList;
    }
}
