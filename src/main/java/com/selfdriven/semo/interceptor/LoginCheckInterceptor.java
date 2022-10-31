package com.selfdriven.semo.interceptor;

import com.selfdriven.semo.dto.login.Login;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Slf4j
@Component
public class LoginCheckInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler)
            throws Exception {
        String requestURI = request.getRequestURI();
        log.info("인증 체크 인터셉터 실행 {}", requestURI);

        HttpSession session = request.getSession();
        Boolean res = false;
        Login login = (Login)session.getAttribute("login");

        // TODO: 인터섭터에서 로그인 인증을 어떻게 체크할 것인지? 그냥 session에 있는 memberId 있는지 체크? 컨트롤러나 서비스를 호출해서 member db 조회?
        if(login != null){
            res = true;
        }
        return res;
    }

}
