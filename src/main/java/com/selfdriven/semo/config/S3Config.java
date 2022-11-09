package com.selfdriven.semo.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
public class S3Config {

    // @Configuration, @Bean이 개발자가 컨트롤이 불가능한 외부 라이브러리 사용을 위해 선택하는 것이라면, @Component는 개발자가 직접 컨트롤이 가능한 내부 클래스에 사용된다고 함.
    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    @Value(value = "product")
    private String productPath;

    @Value(value = "https://semo-image-bucket.s3.ap-northeast-2.amazonaws.com/")
    private String urlBeforePrefix;

}