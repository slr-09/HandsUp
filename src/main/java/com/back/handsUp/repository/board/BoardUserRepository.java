package com.back.handsUp.repository.board;

import com.back.handsUp.domain.board.Board;
import com.back.handsUp.domain.board.BoardUser;
import com.back.handsUp.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BoardUserRepository extends JpaRepository<BoardUser, Long> {
    Optional<BoardUser> findBoardUserByBoardIdx(Long boardIdx);
    Optional<BoardUser> findBoardUserByBoardIdxAndUserIdx(Board boardIdx, User userIdx);
}
