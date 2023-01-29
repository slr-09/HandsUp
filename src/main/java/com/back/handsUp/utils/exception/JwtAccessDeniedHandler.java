package com.back.handsUp.utils.exception;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        // 필요한 권한이 없이 접근하려 할때 403
        setResponse(response, "4048", "접근 권한이 없는 유저입니다.");
    }

    private void setResponse(HttpServletResponse response, String errorCode, String message) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.getWriter().println("{ \"isSuccess\" : " + "false"
                + ", \"statusCode\" : " +  errorCode
                + ", \"message\" : \"" + message +"\"}");
    }
}
