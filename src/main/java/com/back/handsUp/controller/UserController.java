package com.back.handsUp.controller;

import com.back.handsUp.baseResponse.BaseException;
import com.back.handsUp.baseResponse.BaseResponse;
import com.back.handsUp.baseResponse.BaseResponseStatus;
import com.back.handsUp.domain.user.Character;
import com.back.handsUp.dto.jwt.TokenDto;
import com.back.handsUp.dto.user.CharacterDto;
import com.back.handsUp.dto.user.UserCharacterDto;
import com.back.handsUp.dto.user.UserDto;
import com.back.handsUp.service.MailService;
import com.back.handsUp.dto.user.UserNicknameDto;
import com.back.handsUp.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.security.Principal;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/users")
public class UserController {
    private final UserService userService;
    private final MailService mailService;

    @ResponseBody
    @PostMapping("/signup")
    public BaseResponse<String> signUp(@Valid @RequestBody UserDto.ReqSignUp user, BindingResult result){
        if (result.hasErrors()){
            return new BaseResponse<>(BaseResponseStatus.INVALID_REQUEST);
        }

        try {
           this.userService.signupUser(user);
            return new BaseResponse<>("회원가입이 완료되었습니다.");
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    @ResponseBody
    @PostMapping("/login")
    public BaseResponse<TokenDto> logIn(@Valid @RequestBody UserDto.ReqLogIn user, BindingResult result){
        if (result.hasErrors()){
            return new BaseResponse<>(BaseResponseStatus.INVALID_REQUEST);
        }

        try {
            TokenDto token = this.userService.logIn(user);
            return new BaseResponse<>(token);
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    @ResponseBody
    @PostMapping("/logout")
    public BaseResponse<String> logOut(Principal principal){
        try{
            this.userService.logOut(principal);
            return new BaseResponse<>("로그아웃이 완료되었습니다.");
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }



    @ResponseBody
    @PostMapping("create/character")
    public BaseResponse<Long> createCharacter(@RequestBody CharacterDto.GetCharacterInfo characterInfo){

        try{
            Character character = this.userService.createCharacter(characterInfo);
            return new BaseResponse<>(character.getCharacterIdx());
        }catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }

    }


    @ResponseBody
    @PatchMapping("/password")
    public BaseResponse<String> changePwd(Principal principal, @RequestBody UserDto.ReqPwd userPwd) {
        try {
            this.userService.patchPwd(principal, userPwd);
            return new BaseResponse<>("비밀번호 변경이 완료되었습니다.");
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());

        }
    }

    //회원 탈퇴
    @ResponseBody
    @PatchMapping("/withdraw/{userIdx}")
    public BaseResponse<Long> withdrawUser(Principal principal, @PathVariable("userIdx") Long userIdx){
        try{
            this.userService.withdrawUser(principal, userIdx);
            return new BaseResponse<>(userIdx);
        }catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));

        }
    }

    //액세스 토큰 재발급
    @ResponseBody
    @PostMapping("/reissue")
    public BaseResponse<TokenDto> reissue(@RequestBody TokenDto token, HttpServletRequest request) {
        try {
            TokenDto newToken = this.userService.reissue(token, request);
            return new BaseResponse<>(newToken);
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    //학교 인증
    @ResponseBody
    @PostMapping("/certify")
    public BaseResponse<String> certify(@RequestBody UserDto.ResEmail resEmail){
        try {
            String code = this.mailService.sendMail(resEmail.getEmail());
            return new BaseResponse<>(code);
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    @PatchMapping("/nickname")
    public BaseResponse<String> updateUsername(@RequestBody UserNicknameDto userNicknameDto){

        try{
            userService.updateNickname(userNicknameDto.getUserIdx(), userNicknameDto.getNickname());
            return new BaseResponse<>("닉네임을 수정하였습니다.");
        } catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }
    }

    @PatchMapping("/character/{characterIdx}")
    public BaseResponse<String> updateCharacter(@PathVariable Long characterIdx, @RequestBody CharacterDto.GetCharacterInfo characterInfo){
        try{
            userService.updateChatacter(characterIdx, characterInfo);
            return new BaseResponse<>("캐릭터를 수정하였습니다.");
        } catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }
    }

    @GetMapping("/{userIdx}")
    public BaseResponse<UserCharacterDto> getNicknameAndCharacter(@PathVariable Long userIdx){
        try {
            UserCharacterDto userCharacterDto = userService.getUserNicknameCharacter(userIdx);
            return new BaseResponse<>(userCharacterDto);

        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }
}
