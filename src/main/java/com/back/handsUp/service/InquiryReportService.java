package com.back.handsUp.service;

import com.back.handsUp.baseResponse.BaseException;
import com.back.handsUp.baseResponse.BaseResponseStatus;
import com.back.handsUp.domain.board.Board;
import com.back.handsUp.domain.inquiry.Inquiry;
import com.back.handsUp.domain.report.Report;
import com.back.handsUp.domain.user.User;
import com.back.handsUp.dto.inquiryAndReport.InquiryDto;
import com.back.handsUp.dto.inquiryAndReport.ReportDto;
import com.back.handsUp.repository.board.BoardRepository;
import com.back.handsUp.repository.board.BoardUserRepository;
import com.back.handsUp.repository.inquiry.InquiryRepository;
import com.back.handsUp.repository.report.ReportRepository;
import com.back.handsUp.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Slf4j
@Service
@Transactional
public class InquiryReportService {

    private final InquiryRepository inquiryRepository;
    private final ReportRepository reportRepository;
    private final UserRepository userRepository;

    private final BoardUserRepository boardUserRepository;
    private final BoardRepository boardRepository;

    public void inquiry(Principal principal, InquiryDto.PostInquiryInfo postInquiry) throws BaseException {
        Optional<User> optional = this.userRepository.findByEmailAndStatus(principal.getName(), "ACTIVE");

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


    public String reportBoard(Principal principal, ReportDto.PostReportBoardContent postReportBoardContent) throws BaseException {

        String successResult = "게시물을 성공적으로 신고하였습니다";

        Optional<User> optional = this.userRepository.findByEmailAndStatus(principal.getName(), "ACTIVE");

        if(optional.isEmpty()){
            throw new BaseException(BaseResponseStatus.NON_EXIST_EMAIL);
        }

        User user = optional.get();

        Optional<Board> optionalReportedBoard = boardRepository.findByBoardIdx(postReportBoardContent.getBoardIdx());

        if(optionalReportedBoard.isEmpty()){
            throw new BaseException(BaseResponseStatus.NON_EXIST_BOARDIDX);
        }

        Board reportedBoard = optionalReportedBoard.get();

        Optional<User> optionalReportedUser = boardUserRepository.findUserIdxByBoardIdxAndStatus(postReportBoardContent.getBoardIdx(), "WRITE");

        if(optionalReportedUser.isEmpty()){
            throw new BaseException(BaseResponseStatus.NON_EXIST_EMAIL);
        }

        User reportedUser = optionalReportedUser.get();

        if (Objects.equals(user.getUserIdx(), reportedUser.getUserIdx())) {
            throw new BaseException(BaseResponseStatus.SELF_REPORT_ERROR);
        }

        ReportDto.PostReportBoardInfo postReportBoardInfo = ReportDto.PostReportBoardInfo.builder()
                .ReportedUser(reportedUser)
                .content(postReportBoardContent.getContent())
                .build();

        if (checkReportDuplicate(user, reportedUser, reportedBoard)) {
            throw new BaseException(BaseResponseStatus.REPORTED_ERROR);
        }

        log.info("-------------------report builder start--------------------");
        Report report = Report.builder()
                .contents(postReportBoardInfo.getContent())
                .reportedUser(postReportBoardInfo.getReportedUser())
                .user(user)
                .reportedBoard(reportedBoard)
                .status("BOARD")
                .build();
        log.info("--------------------report ={}-------------------------", report);

        try {
            this.reportRepository.save(report);
            return successResult;
        } catch (Exception e) {
            throw new BaseException(BaseResponseStatus.DATABASE_INSERT_ERROR);
        }

    }

    public String reportUser(Principal principal, ReportDto.PostReportUserContent postReportUserContent) throws BaseException {

        String successResult = "유저를 성공적으로 신고하였습니다";

        Optional<User> optional = this.userRepository.findByEmailAndStatus(principal.getName(), "ACTIVE");

        if(optional.isEmpty()){
            throw new BaseException(BaseResponseStatus.NON_EXIST_EMAIL);
        }

        User user = optional.get();


        Optional<User> optionalReportedUser = this.userRepository.findByUserIdx(postReportUserContent.getUserIdx());

        if(optionalReportedUser.isEmpty()){
            throw new BaseException(BaseResponseStatus.NON_EXIST_EMAIL);
        }

        User reportedUser = optionalReportedUser.get();

        if (Objects.equals(user.getUserIdx(), reportedUser.getUserIdx())) {
            throw new BaseException(BaseResponseStatus.SELF_REPORT_ERROR);
        }

        if (checkReportDuplicate(user, reportedUser, null)) {
            throw new BaseException(BaseResponseStatus.REPORTED_ERROR);
        }

        ReportDto.PostReportUserInfo postReportUserInfo = ReportDto.PostReportUserInfo.builder()
                .ReportedUser(reportedUser)
                .content(postReportUserContent.getContent())
                .build();


        log.info("-------------------report builder start--------------------");
        Report report = Report.builder()
                .contents(postReportUserInfo.getContent())
                .reportedUser(postReportUserInfo.getReportedUser())
                .user(user)
                .status("USER")
                .build();
        log.info("--------------------report ={}-------------------------", report);

        try {
            this.reportRepository.save(report);
            return successResult;
        } catch (Exception e) {
            throw new BaseException(BaseResponseStatus.DATABASE_INSERT_ERROR);
        }

    }

    private boolean checkReportDuplicate(User user, User reportedUser, Board reportedBoard) {
        boolean checkUser = reportRepository.existsByUser(user);
        boolean checkReportedUser = reportRepository.existsByReportedUser(reportedUser);
        boolean checkReportedBoard = reportRepository.existsByReportedBoard(reportedBoard);
        return checkUser && checkReportedUser && checkReportedBoard;
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

    public List<ReportDto.GetAllReport> getNotSolvedReport(Principal principal) throws BaseException {

        List<ReportDto.GetAllReport> getNotSolvedReport = reportRepository.findAllByStatusNot("SOLVED").stream().map(Report::ToGetReport).collect(Collectors.toList());

        return getNotSolvedReport;
    }
}
