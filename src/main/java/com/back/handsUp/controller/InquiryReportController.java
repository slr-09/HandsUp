package com.back.handsUp.controller;

import com.back.handsUp.baseResponse.BaseException;
import com.back.handsUp.baseResponse.BaseResponse;
import com.back.handsUp.dto.inquiryAndReport.InquiryDto;
import com.back.handsUp.dto.inquiryAndReport.ReportDto;
import com.back.handsUp.service.InquiryReportService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("/help")
public class InquiryReportController {

    private final InquiryReportService inquiryReportService;

    @PostMapping("/inquiry")
    public BaseResponse<String> inquiry(Principal principal, @RequestBody InquiryDto.PostInquiryInfo postInquiryInfo) {
        try {
            this.inquiryReportService.inquiry(principal, postInquiryInfo);
            return new BaseResponse<>("문의가 접수되었습니다.");
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    @PostMapping("/report")
    public BaseResponse<String> report(Principal principal, @RequestBody ReportDto.PostReportContent postReportContent) {
        try {
            this.inquiryReportService.report(principal, postReportContent);
            return new BaseResponse<>("신고가 접수되었습니다.");
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }
}
