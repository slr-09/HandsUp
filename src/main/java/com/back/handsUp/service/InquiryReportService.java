package com.back.handsUp.service;

import com.back.handsUp.baseResponse.BaseException;
import com.back.handsUp.baseResponse.BaseResponseStatus;
import com.back.handsUp.domain.inquiry.Inquiry;
import com.back.handsUp.domain.report.Report;
import com.back.handsUp.domain.user.User;
import com.back.handsUp.dto.inquiryAndReport.InquiryDto;
import com.back.handsUp.dto.inquiryAndReport.ReportDto;
import com.back.handsUp.repository.board.BoardUserRepository;
import com.back.handsUp.repository.inquiry.InquiryRepository;
import com.back.handsUp.repository.report.ReportRepository;
import com.back.handsUp.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotBlank;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
@Service
public class InquiryReportService {

    private final InquiryRepository inquiryRepository;
    private final ReportRepository reportRepository;
    private final UserRepository userRepository;

    private final BoardUserRepository boardUserRepository;

    public void inquiry(Principal principal, InquiryDto.PostInquiryInfo postInquiry) throws BaseException {
        Optional<User> optional = this.userRepository.findByEmail(principal.getName());

        if(optional.isEmpty()){
            throw new BaseException(BaseResponseStatus.NON_EXIST_EMAIL);
        }

        User user = optional.get();

        log.info("----------inquriy builder start---------------");
        Inquiry inquiry = Inquiry.builder()
                                .contents(postInquiry.getContents())
                                .user(user)
                                .build();
        log.info("----------------inquiry = {}------------------",inquiry);
        try {
            this.inquiryRepository.save(inquiry);
        } catch (Exception e) {
            throw new BaseException(BaseResponseStatus.DATABASE_INSERT_ERROR);
        }
    }


    public void report(Principal principal, ReportDto.PostReportContent postReportContent) throws BaseException {
        Optional<User> optional = this.userRepository.findByEmail(principal.getName());

        if(optional.isEmpty()){
            throw new BaseException(BaseResponseStatus.NON_EXIST_EMAIL);
        }

        User user = optional.get();


        Optional<User> optionalReportedUser = boardUserRepository.findUserIdxByBoardIdxAndStatus(postReportContent.getBoardIdx(), "WRITE");

        if(optionalReportedUser.isEmpty()){
            throw new BaseException(BaseResponseStatus.NON_EXIST_EMAIL);
        }

        User reportedUser = optionalReportedUser.get();

        ReportDto.PostReportInfo postReportInfo = ReportDto.PostReportInfo.builder()
                .ReportedUser(reportedUser)
                .content(postReportContent.getContent())
                .build();


        log.info("-------------------report builder start--------------------");
        Report report = Report.builder()
                .contents(postReportInfo.getContent())
                .reportedUser(postReportInfo.getReportedUser())
                .user(user)
                .build();
        log.info("--------------------report ={}-------------------------", report);

        try {
            this.reportRepository.save(report);
        } catch (Exception e) {
            throw new BaseException(BaseResponseStatus.DATABASE_INSERT_ERROR);
        }

    }

    //문의 조회 (관리자용)
    public List<InquiryDto.PostInquiryInfo> getInquiry(Principal principal) throws BaseException{

        List<Inquiry> getInquiry = inquiryRepository.findAll();

        List<InquiryDto.PostInquiryInfo> getInquiryInfo = new ArrayList<>();

        for (Inquiry i : getInquiry){
            InquiryDto.PostInquiryInfo dto = InquiryDto.PostInquiryInfo.builder()
                    .contents(i.getContents())
                    .build();

            getInquiryInfo.add(dto);
        }

        return getInquiryInfo;
    }

    //신고 조회 (관리자용)
    public List<ReportDto.GetReport> getReport(Principal principal) throws BaseException{

        List<Report> getReport = reportRepository.findAll();

        List<ReportDto.GetReport> getReportInfo = new ArrayList<>();

        for (Report r : getReport){
            ReportDto.GetReport dto = ReportDto.GetReport.builder()
                    .reportedUserIdx(r.getReportedUser().getUserIdx())
                    .content(r.getContents())
                    .build();

            getReportInfo.add(dto);
        }

        return getReportInfo;
    }
}
