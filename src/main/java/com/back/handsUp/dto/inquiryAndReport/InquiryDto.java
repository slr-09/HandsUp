package com.back.handsUp.dto.inquiryAndReport;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@NoArgsConstructor
public class InquiryDto {

    @Getter
    @Builder
    @AllArgsConstructor
    public static class PostInquiryInfo {
        private String contents;
        public PostInquiryInfo() {
        }
    }

}
