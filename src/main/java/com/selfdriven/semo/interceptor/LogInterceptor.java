package com.selfdriven.semo.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.springframework.util.ObjectUtils.isEmpty;

@Slf4j
@Component
public class LogInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler) {
        String requestURI = request.getRequestURI();
        log.info("REQUEST [{}][{}][{}]", requestURI, handler);
        return true;
    }

    @Override
    public void postHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler,
            ModelAndView modelAndView) {
        log.info("postHandle [{}]", modelAndView);
    }

    @Override
    public void afterCompletion(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler,
            Exception ex
    ) {
        String requestURI = request.getRequestURI();
        log.info("RESPONSE [{}][{}][{}]", requestURI, handler);

        if (!isEmpty(ex)) {
            log.error("afterCompletion error!!", ex);
        }
    }

}
