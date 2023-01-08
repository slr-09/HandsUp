package com.back.handsUp.mapper;

import com.back.handsUp.domain.board.Board;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class BoardMapper implements RowMapper<Board> {

    @Override
    public Board mapRow(ResultSet rs, int rowNum) throws SQLException {

        Board board = new Board();

        board.setBoardIdx(rs.getInt("boardIdx"));
        board.setContent(rs.getString("boardContents"));
        board.setMessageDuration(rs.getInt("messageDuration"));
        board.setLocation(rs.getString("location"));
        board.setIndicateLocation(rs.getString("indicatedLocation"));
        board.setStatus(rs.getString("status"));
        board.setCreatedAt(rs.getTimestamp("createdAt").toLocalDateTime());
        board.setUpdatedAt(rs.getTimestamp("updatedAt").toLocalDateTime());

        return board;
    }
}
