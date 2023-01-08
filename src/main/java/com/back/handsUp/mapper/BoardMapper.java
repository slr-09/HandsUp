package com.back.handsUp.mapper;

import com.back.handsUp.domain.board.Board;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class BoardMapper implements RowMapper<Board> {

    @Override
    public Board mapRow(ResultSet rs, int rowNum) throws SQLException {

        Board board = new Board();

        board.setId(rs.getInt("boardIdx"));
        board.setContents(rs.getString("boardContents"));
        board.setDuration(rs.getInt("messageDuration"));

        return board;
    }
}
