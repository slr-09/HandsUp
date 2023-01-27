package com.back.handsUp.dto.inquiryAndReport;

import com.back.handsUp.domain.board.Board;
import com.back.handsUp.domain.user.User;
import lombok.*;

@NoArgsConstructor
public class ReportDto {

    @Getter
    @Builder
    @AllArgsConstructor
    public static class PostReportUserInfo{
        private String content;
        private User ReportedUser;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    public static class PostReportBoardInfo{
        private String content;
        private User ReportedUser;
        private Board ReportedBoard;
    }


    @Getter
    @AllArgsConstructor
    public static class PostReportBoardContent{
        private String content;
        private Long boardIdx;
    }

    @Getter
    @AllArgsConstructor
    public static class PostReportUserContent{
        private String content;
        private Long userIdx;
    }
}
