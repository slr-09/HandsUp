package com.back.handsUp.repository.board;

import com.back.handsUp.domain.board.Board;
import com.back.handsUp.domain.board.BoardUser;
import com.back.handsUp.domain.user.School;
import com.back.handsUp.domain.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface BoardUserRepository extends JpaRepository<BoardUser, Long> {

    Optional<BoardUser> findBoardUserByBoardIdx(Board boardIdx);

    @Query("select b.boardIdx from BoardUser b where b.userIdx = ?1 and b.status = ?2")
    List<Board> findBoardIdxByUserIdxAndStatus(User userIdx, String status);

    @Query("select b.userIdx from BoardUser b where b.boardIdx.boardIdx = ?1 and b.status = ?2 ")
    Optional<User> findUserIdxByBoardIdxAndStatus(Long boardIdx, String status);

    Optional<BoardUser> findBoardUserByBoardIdxAndUserIdx(Board boardIdx, User userIdx);

    @Query("select b from BoardUser b where b.userIdx.schoolIdx = ?1 and b.boardIdx.status = ?2")
    List<BoardUser> findBoardBySchoolAndStatus(School schoolIdx, String status);

    @Query("select b from BoardUser b where b.boardIdx.status = ?1")
    List<BoardUser> findBoardUserByStatus(String status);

    List<BoardUser> findBoardUserByBoardIdxAndStatus(Board boardIdx, String status);

    Page<BoardUser> findByBoardUserIdxLessThanAndStatusAndBoardIdxInOrderByBoardUserIdxDesc(Long boardUserIdx, String status, List<Board> boardList, PageRequest pageRequest);

    Page<BoardUser> findAllByStatusAndBoardIdxInOrderByBoardUserIdxDesc(String status, List<Board> boardList, PageRequest pageRequest);

    @Query("select bu.boardIdx from BoardUser bu inner join Board b  on b.boardIdx = bu.boardIdx.boardIdx where bu.userIdx = ?1 and bu.status = ?2 and b.status = ?3 ")
    Page<Board> findBoardIdxByUserIdxAndStatusInOrderByBoardUserIdxDesc(User userIdx, String status, String boardStatus, Pageable pageable);

    Page<BoardUser> findByUserIdxAndStatus(User userIdx, String status, Pageable pageable);

    Optional<BoardUser> findByUserIdxAndBoardIdxAndStatus(User userIdx, Board boardIdx, String status);

    @Query("select b from BoardUser b where b.boardIdx.status = ?1")
    Page<BoardUser> findBoardUserIdxByStatusInOrderByBoardUserIdxDesc(String status, Pageable pageable);
}

