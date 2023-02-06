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
        board.setContent(rs.getString("content"));
        board.setMessageDuration(rs.getInt("messageDuration"));
        board.setLatitude(rs.getDouble("latitude"));
        board.setLongitude(rs.getDouble("longitude"));
        board.setIndicateLocation(rs.getString("indicatedLocation"));
        board.setStatus(rs.getString("status"));
        board.setCreatedAt(rs.getTimestamp("createdAt").toLocalDateTime());
        board.setUpdatedAt(rs.getTimestamp("updatedAt").toLocalDateTime());

        return board;
    }
}
