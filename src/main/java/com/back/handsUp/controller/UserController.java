package com.back.handsUp.controller;

import com.back.handsUp.baseResponse.BaseException;
import com.back.handsUp.baseResponse.BaseResponse;
import com.back.handsUp.baseResponse.BaseResponseStatus;
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
    public BaseResponse<String> createCharacter(@RequestBody CharacterDto.GetCharacterInfo characterInfo){

        try{
            this.userService.createCharacter(characterInfo);
            return new BaseResponse<>("캐릭터가 생성되었습니다.");
        }catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }

    }

    @ResponseBody
    @PatchMapping("/password")
    public BaseResponse<String> changePwd(Principal principal, @RequestBody UserDto.ReqPwd userPwd){
        try {
            this.userService.patchPwd(principal, userPwd);
            return new BaseResponse<>("비밀번호 변경이 완료되었습니다.");
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }
}
