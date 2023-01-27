package com.back.handsUp.repository.report;

import com.back.handsUp.domain.board.Board;
import com.back.handsUp.domain.report.Report;
import com.back.handsUp.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {

    boolean existsByUser(User user);

    boolean existsByReportedUser(User reportedUser);

    boolean existsByReportedBoard(Board reportedBoard);
}
