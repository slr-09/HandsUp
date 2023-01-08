package com.back.handsUp.repository;

import com.back.handsUp.domain.board.Board;
import com.back.handsUp.mapper.BoardMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;

@Repository
public class BoardRepository {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public Board boardViewByIdx(int idx) {
        String sql = "SELECT * FROM board WHERE boardIdx = ?";
        Board board = jdbcTemplate.queryForObject(sql, new Object[]{idx},new BoardMapper());
        return board;
    }
}
