package com.back.handsUp.repository.board;

import com.back.handsUp.domain.board.Board;
import com.back.handsUp.domain.board.BoardUser;
import com.back.handsUp.domain.user.School;
import com.back.handsUp.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

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

    @Query("select b.boardIdx from BoardUser b where b.userIdx.schoolIdx = ?1 and b.boardIdx.status = ?2")
    List<Board> findBoardBySchoolAndStatus(School schoolIdx, String status);

    List<BoardUser> findBoardUserByBoardIdxAndStatus(Board boardIdx, String status);

}

