package com.back.handsUp.utils.exception;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        // 유효한 자격증명을 제공하지 않고 접근하려 할때 401
//        response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
        String exception =(String)request.getAttribute("exception");
        if (exception == null) {
            setResponse(response, "4047", "JWT 토큰에 오류가 발생했습니다");
        } else if(exception.equals("4043")){
            setResponse(response, exception, "잘못된 JWT 서명입니다.");
        } else if (exception.equals("4044")) {
            setResponse(response, exception, "만료된 JWT 토큰입니다.");
        } else if (exception.equals("4045")) {
            setResponse(response, exception, "지원되지 않는 JWT 토큰입니다.");
        } else if (exception.equals("4046")) {
            setResponse(response, exception, "JWT 토큰이 잘못되었습니다.");
        } else {
            setResponse(response, "4047", "JWT 토큰에 오류가 발생했습니다");
        }
    }

    private void setResponse(HttpServletResponse response, String errorCode, String message) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().println("{ \"isSuccess\" : " + "false"
                + ", \"statusCode\" : " +  errorCode
                + ", \"message\" : \"" + message +"\"}");
    }
}
