package com.selfdriven.semo.service;

import java.io.IOException;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.selfdriven.semo.dto.ImageProduct;
import com.selfdriven.semo.mapper.ProductImageMapper;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class S3UploadService {
	 @Value("${cloud.aws.s3.bucket}")
		private String bucket;

		private final ProductImageMapper productImageMapper;

		private final AmazonS3 amazonS3;

	    public String upload(String route, MultipartFile multipartFile) throws IOException {
	        String s3FileName = route+ "/" + UUID.randomUUID() + "-" + multipartFile.getOriginalFilename();
	        ObjectMetadata objMeta = new ObjectMetadata();
	        objMeta.setContentLength(multipartFile.getInputStream().available());
	        amazonS3.putObject(bucket, s3FileName, multipartFile.getInputStream(), objMeta);
	        return amazonS3.getUrl(bucket, s3FileName).toString();
	    }

	    public int insertProductImage(ImageProduct image) {
	    	
	    	        return productImageMapper.insertProductImage(image);
	    	    
	    }
}
