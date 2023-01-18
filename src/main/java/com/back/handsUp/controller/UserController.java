package com.back.handsUp.controller;

import com.back.handsUp.baseResponse.BaseException;
import com.back.handsUp.baseResponse.BaseResponse;
import com.back.handsUp.baseResponse.BaseResponseStatus;
import com.back.handsUp.domain.user.Character;
import com.back.handsUp.dto.jwt.TokenDto;
import com.back.handsUp.dto.user.CharacterDto;
import com.back.handsUp.dto.user.UserDto;
import com.back.handsUp.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/users")
public class UserController {
    private final UserService userService;

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
    @PostMapping("create/character")
    public BaseResponse<Long> createCharacter(@RequestBody CharacterDto.GetCharacterInfo characterInfo){

        try{
            Character character = this.userService.createCharacter(characterInfo);
            return new BaseResponse<>(character.getCharacterIdx());
        }catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
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
}
