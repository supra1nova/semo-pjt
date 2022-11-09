package com.selfdriven.semo.interceptor;

import com.selfdriven.semo.dto.login.Login;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.users.SparseUserDatabase;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@Component
public class LoginCheckInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestURI = request.getRequestURI();
        log.info("인증 체크 인터셉터 실행 {}", requestURI);

        HttpSession session = request.getSession();
        Boolean res = false;
        try {
            res = ((Login)(session.getAttribute("login"))).getId() != null ? true : false;
        } catch(Exception e) {
            log.warn("=================== NOT ALLOWED TO ACCESS WITHOUT LOGIN ===================");
            response.sendRedirect("/login-out");
        }
        return res;
    }

}
