package com.selfdriven.semo.config;

import com.selfdriven.semo.interceptor.CustomerCheckInterceptor;
import com.selfdriven.semo.interceptor.LogInterceptor;
import com.selfdriven.semo.interceptor.LoginCheckInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

//    @Override
//    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
//        resolvers.add(new LoginMemberArgumentResolver());
//    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LogInterceptor())
            .order(1)
            .addPathPatterns("/**")
            .excludePathPatterns("/static/**", "/template/**", "/*.ico");

        registry.addInterceptor(new LoginCheckInterceptor())
            .order(2)
            .addPathPatterns("/api/room-image/upload/**", "/api/room-image/delete/**")
            .excludePathPatterns(
                    "/api/room-image/load-one/**", "/api/room-image/load-all/**");

        registry.addInterceptor(new CustomerCheckInterceptor())
            .order(3)
            .addPathPatterns("/api/room-image/upload/**", "/api/room-image/delete/**")
            .excludePathPatterns(
                    "/api/room-image/load-one/**", "/api/room-image/load-all/**");
    }

}
