package com.selfdriven.semo.config;

import com.selfdriven.semo.interceptor.CustomerCheckInterceptor;
import com.selfdriven.semo.interceptor.LogInterceptor;
import com.selfdriven.semo.interceptor.LoginCheckInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LogInterceptor())
            .order(1)
            .addPathPatterns("/**")
            .excludePathPatterns(
                    "/resources/**", "/template/**", "/*.ico");

        registry.addInterceptor(new LoginCheckInterceptor())
            .order(2)
            .addPathPatterns(
                    "/api/member/list/**", "/api/member/info/**", "/api/member/edit/**", "/api/member/withdraw/**",
                    "/api/product/create/**", "/api/product/delete/**", "/api/product/edit/**",
                    "/api/room/create/**", "/api/room/delete/**", "/api/room/edit/**",
                    "/api/room-image/upload/**", "/api/room-image/delete/**",
                    "/api/product-image/upload/**", "/api/product-image/delete/**",
                    "/api/rent-info/create/**", "/api/rent-info/update/**", "/api/rent-info/delete/**",
                    "/api/reservation/create/**", "/api/reservation/update/**", "/api/reservation/delete/**"
            )
            .excludePathPatterns(
//                    "/resources/**", "/api/member/join/**", "/api/room-image/load-one/**", "/api/room-image/load-all/**", "/api/product-image/load-one/**", "/api/product-image/load-all/**");
                    "/resources/**");

        registry.addInterceptor(new CustomerCheckInterceptor())
            .order(3)
            .addPathPatterns(
                    "/api/room-image/upload/**", "/api/room-image/delete/**", "/api/product-image/upload/**", "/api/product-image/delete/**",
                    "/api/product/create/**", "/api/product/delete/**", "/api/product/edit/**",
                    "/api/room/create/**", "/api/room/delete/**", "/api/room/edit/**",
                    "/api/room-image/upload/**", "/api/room-image/delete/**",
                    "/api/product-image/upload/**", "/api/product-image/delete/**",
                    "/api/rent-info/create/**", "/api/rent-info/update/**", "/api/rent-info/delete/**",
                    "/api/reservation/create/**", "/api/reservation/update/**", "/api/reservation/delete/**"
            )
            .excludePathPatterns(
//                    "/resources/**", "/api/room-image/load-one/**", "/api/room-image/load-all/**", "/api/product-image/load-one/**", "/api/product-image/load-all/**");
                    "/resources/**");
    }

}
