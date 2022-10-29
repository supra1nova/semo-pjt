package com.selfdriven.semo.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Setter
@Getter
@Component
public class S3Component {

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    @Value(value = "product")
    private String productPath;

    @Value(value = "product/room")
    private String productRoomPath;

}