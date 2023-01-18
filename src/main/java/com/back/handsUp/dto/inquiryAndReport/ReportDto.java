package com.back.handsUp.dto.inquiryAndReport;

import com.back.handsUp.domain.user.User;
import lombok.*;

@NoArgsConstructor
public class ReportDto {

    @Getter
    @Builder
    @AllArgsConstructor
    public static class PostReportInfo{
        private String content;
        private User ReportedUser;
    }

    @Getter
    @AllArgsConstructor
    public static class PostReportContent{
        private String content;
        private Long boardIdx;
    }

}
