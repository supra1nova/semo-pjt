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
public class CustomerCheckInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {

        String requestURI = request.getRequestURI();
        log.info("인가 체크 인터셉터 실행 {}", requestURI);

        HttpSession session = request.getSession();
        Login login = (Login)session.getAttribute("login");
        Boolean res = false;
        if(login != null){
            res = login.getMemberType().equals("c") ? true : false;
        } else {
            // TODO: 실패시 어디로 보내야하는지...?
        }
        return res;
    }

}
